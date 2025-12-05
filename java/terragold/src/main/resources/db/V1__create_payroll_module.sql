-- V1__create_payroll_module_fixed_allowances.sql


-- Optional: add allowance columns to employees if not present
ALTER TABLE employees
    ADD COLUMN IF NOT EXISTS base_salary DECIMAL(18,2) DEFAULT 0.00,
    ADD COLUMN IF NOT EXISTS housing_allowance DECIMAL(18,2) DEFAULT 0.00,
    ADD COLUMN IF NOT EXISTS transport_allowance DECIMAL(18,2) DEFAULT 0.00,
    ADD COLUMN IF NOT EXISTS other_fixed_allowance DECIMAL(18,2) DEFAULT 0.00;


-- Payroll runs
CREATE TABLE IF NOT EXISTS payroll_runs (
                                            id BIGSERIAL PRIMARY KEY,
                                            period_year INT NOT NULL,
                                            period_month INT NOT NULL,
                                            payroll_month VARCHAR(7) NOT NULL, -- 'YYYY-MM' convenience
                                            apply_pension BOOLEAN NOT NULL DEFAULT TRUE,
                                            apply_tax BOOLEAN NOT NULL DEFAULT TRUE,
                                            status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                                            generated_at TIMESTAMPTZ DEFAULT NOW(),
                                            approved_at TIMESTAMPTZ,
                                            approved_by BIGINT,
                                            paid_at TIMESTAMPTZ,
                                            notes TEXT,
                                            UNIQUE(period_year, period_month)
);


-- Payroll inputs (overtime/bonuses/deductions)
CREATE TABLE IF NOT EXISTS payroll_inputs (
                                              id BIGSERIAL PRIMARY KEY,
                                              employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
                                              period_year INT NOT NULL,
                                              period_month INT NOT NULL,
                                              overtime_hours DECIMAL(10,2) DEFAULT 0,
                                              overtime_amount DECIMAL(18,2) DEFAULT 0,
                                              bonus_amount DECIMAL(18,2) DEFAULT 0,
                                              deduction_amount DECIMAL(18,2) DEFAULT 0,
                                              notes TEXT,
                                              created_at TIMESTAMPTZ DEFAULT NOW(),
                                              updated_at TIMESTAMPTZ DEFAULT NOW(),
                                              UNIQUE(employee_id, period_year, period_month)
);


-- Payroll items (per-employee lines)
CREATE TABLE IF NOT EXISTS payroll_items (
                                             id BIGSERIAL PRIMARY KEY,
                                             payroll_run_id BIGINT NOT NULL REFERENCES payroll_runs(id) ON DELETE CASCADE,
                                             employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,


                                             base_salary DECIMAL(18,2) NOT NULL,
                                             housing_allowance DECIMAL(18,2) DEFAULT 0,
                                             transport_allowance DECIMAL(18,2) DEFAULT 0,
                                             other_fixed_allowance DECIMAL(18,2) DEFAULT 0,
                                             overtime_amount DECIMAL(18,2) DEFAULT 0,
                                             bonus_amount DECIMAL(18,2) DEFAULT 0,


                                             tax_amount DECIMAL(18,2) DEFAULT 0,
                                             pension_employee DECIMAL(18,2) DEFAULT 0,
                                             pension_employer DECIMAL(18,2) DEFAULT 0,
                                             other_deductions DECIMAL(18,2) DEFAULT 0,
                                             net_pay DECIMAL(18,2) NOT NULL,

                                             status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                                             created_at TIMESTAMPTZ DEFAULT NOW(),
                                             updated_at TIMESTAMPTZ DEFAULT NOW()
);


-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_payroll_items_run ON payroll_items(payroll_run_id);
CREATE INDEX IF NOT EXISTS idx_payroll_inputs_emp_period ON payroll_inputs(employee_id, period_year, period_month);