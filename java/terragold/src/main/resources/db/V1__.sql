-- DROP EVERYTHING (fresh start)
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO current_user;

-- Enable useful extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Users (your login accounts)
CREATE TABLE users (
                       id         BIGSERIAL PRIMARY KEY,
                       username   VARCHAR(50)  UNIQUE NOT NULL,
                       password   VARCHAR(255) NOT NULL,
                       role       VARCHAR(50)  NOT NULL,        -- ADMIN, MANAGER, ACCOUNTANT, etc.
                       created_at TIMESTAMPTZ DEFAULT NOW(),
                       updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 2. Employees (formal staff + purchasers + agents)
CREATE TABLE employees (
                           id               BIGSERIAL PRIMARY KEY,
                           employee_code    VARCHAR(20)  UNIQUE NOT NULL,   -- EMP-001
                           full_name        VARCHAR(100) NOT NULL,
                           phone_number     VARCHAR(20),
                           role             VARCHAR(50),                    -- Purchaser, Manager, Chemist, Guard...
                           department       VARCHAR(50),
                           hire_date        DATE,
                           termination_date DATE,
                           monthly_salary   DECIMAL(18,2),
                           notes            TEXT,
                           created_at       TIMESTAMPTZ DEFAULT NOW(),
                           updated_at       TIMESTAMPTZ DEFAULT NOW()
);

-- 3. Item Types (fuel, chemicals, spare parts, tools…)
CREATE TABLE item_types (
                            id                  BIGSERIAL PRIMARY KEY,
                            name                VARCHAR(100) UNIQUE NOT NULL,
                            description         TEXT,
                            unit_of_measurement VARCHAR(20) NOT NULL,        -- LITER, KG, PIECE, etc.
                            created_at          TIMESTAMPTZ DEFAULT NOW(),
                            updated_at          TIMESTAMPTZ DEFAULT NOW()
);

-- 4. Machines / Assets
CREATE TABLE machines (
                          id               BIGSERIAL PRIMARY KEY,
                          machine_type     VARCHAR(100),
                          asset_machine_id VARCHAR(50) UNIQUE,
                          description      TEXT,
                          created_at       TIMESTAMPTZ DEFAULT NOW(),
                          updated_at       TIMESTAMPTZ DEFAULT NOW()
);

-- 5. Employee Advance Accounts (one running balance per agent)
CREATE TABLE employee_advance_accounts (
                                           id              BIGSERIAL PRIMARY KEY,
                                           employee_id     BIGINT UNIQUE NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
                                           current_balance DECIMAL(18,2) DEFAULT 0.00 NOT NULL,
                                           is_active       BOOLEAN DEFAULT true,
                                           created_at      TIMESTAMPTZ DEFAULT NOW(),
                                           updated_at      TIMESTAMPTZ DEFAULT NOW()
);

-- 6. Every single cash movement to/from agents
CREATE TABLE employee_advance_transactions (
                                               id                 BIGSERIAL PRIMARY KEY,
                                               account_id         BIGINT NOT NULL REFERENCES employee_advance_accounts(id),
                                               transaction_date   DATE NOT NULL,
                                               type               VARCHAR(10) NOT NULL CHECK (type IN ('TOP_UP', 'EXPENSE')),
                                               amount             DECIMAL(18,2) NOT NULL,
                                               description        VARCHAR(255),
                                               receipt_ref        VARCHAR(100),
                                               recorded_by       BIGINT REFERENCES users(id),
                                               balance_before     DECIMAL(18,2) NOT NULL,
                                               balance_after      DECIMAL(18,2) NOT NULL,
                                               created_at         TIMESTAMPTZ DEFAULT NOW()
);

-- 7. Daily / Casual Labor Payments (the 50–200 people you pay cash every day)
CREATE TABLE daily_labor_payments (
                                      id                     BIGSERIAL PRIMARY KEY,
                                      payment_date           DATE NOT NULL,
                                      full_name              VARCHAR(100) NOT NULL,
                                      id_or_phone            VARCHAR(30),
                                      role                   VARCHAR(50),
                                      daily_rate             DECIMAL(12,2),
                                      days_worked            INTEGER DEFAULT 1,
                                      total_paid             DECIMAL(18,2) NOT NULL,
                                      paid_by_employee_id    BIGINT REFERENCES employees(id),
                                      advance_transaction_id BIGINT REFERENCES employee_advance_transactions(id),
                                      notes                  TEXT,
                                      created_at             TIMESTAMPTZ DEFAULT NOW()
);

-- 8. General / Miscellaneous Expenses (rent, office fuel, bank charges…)
CREATE TABLE general_expenses (
                                  id                     BIGSERIAL PRIMARY KEY,
                                  expense_date           DATE NOT NULL,
                                  category               VARCHAR(50) NOT NULL,
                                  vendor_or_paid_to      VARCHAR(150),
                                  description            VARCHAR(255) NOT NULL,
                                  amount                 DECIMAL(18,2) NOT NULL,
                                  receipt_ref            VARCHAR(100),
                                  paid_by_employee_id    BIGINT REFERENCES employees(id),
                                  advance_transaction_id BIGINT REFERENCES employee_advance_transactions(id),
                                  created_at             TIMESTAMPTZ DEFAULT NOW()
);

-- 9. Inventory Purchases (chemicals, spare parts, etc.)
CREATE TABLE inventory_purchases (
                                     id                  BIGSERIAL PRIMARY KEY,
                                     purchase_date       DATE NOT NULL,
                                     supplier_name       VARCHAR(150) NOT NULL,
                                     item_type_id        BIGINT NOT NULL REFERENCES item_types(id),
                                     quantity_purchased  DECIMAL(12,3) NOT NULL,
                                     unit_cost           DECIMAL(18,2) NOT NULL,
                                     total_cost          DECIMAL(18,2) NOT NULL,
                                     receipt_invoice_no  VARCHAR(100),
                                     purchased_by_employee_id BIGINT REFERENCES employees(id),
                                     advance_transaction_id   BIGINT REFERENCES employee_advance_transactions(id),
                                     created_at          TIMESTAMPTZ DEFAULT NOW()
);

-- 10. Inventory Consumption / Issue to machines
CREATE TABLE inventory_consumptions (
                                        id              BIGSERIAL PRIMARY KEY,
                                        issue_date      DATE NOT NULL,
                                        item_type_id    BIGINT NOT NULL REFERENCES item_types(id),
                                        quantity_issued DECIMAL(12,3) NOT NULL,
                                        machine_id      BIGINT NOT NULL REFERENCES machines(id),
                                        issued_by_employee_id BIGINT REFERENCES employees(id),
                                        notes           TEXT,
                                        created_at      TIMESTAMPTZ DEFAULT NOW()
);

-- 11. Gold Production
CREATE TABLE production (
                            id              BIGSERIAL PRIMARY KEY,
                            production_date DATE NOT NULL,
                            quantity_grams  DECIMAL(12,3) NOT NULL,
                            grade_purity    DECIMAL(5,2),
                            source_batch_id VARCHAR(100),
                            operator_id     BIGINT REFERENCES employees(id),
                            notes           TEXT,
                            created_at      TIMESTAMPTZ DEFAULT NOW()
);

-- Indexes (super fast reports)
CREATE INDEX idx_advance_tx_date        ON employee_advance_transactions(transaction_date DESC);
CREATE INDEX idx_advance_account_emp    ON employee_advance_accounts(employee_id);
CREATE INDEX idx_daily_labor_date       ON daily_labor_payments(payment_date);
CREATE INDEX idx_general_exp_date      general_expenses(expense_date);
CREATE INDEX idx_inventory_cons_date   ON inventory_consumptions(issue_date);
CREATE INDEX idx_production_date       ON production(production_date DESC);

-- Done. You now have the cleanest, most professional gold-mining database in Ethiopia.