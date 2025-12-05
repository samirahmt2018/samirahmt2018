package com.gundan.terragold.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.math.BigDecimal;

import com.gundan.terragold.enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "general_expenses")
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class GeneralExpense extends AbstractExpense {

    private String vendor;
    private String description;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String receiptAttachment;
    private String paidTo; // name if not formal vendor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_by_employee_id")
    private Employee paidBy;

    private String notes;// who handed the cash

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advance_transaction_id")
    private EmployeeAdvanceTransaction advanceTransaction;   // ← links it to the agent’s balance

}