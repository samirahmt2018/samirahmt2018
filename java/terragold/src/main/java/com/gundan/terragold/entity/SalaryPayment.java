package com.gundan.terragold.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import com.gundan.terragold.enums.ExpenseCategory;

@Entity
@Table(name = "salary_payments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SalaryPayment extends AbstractExpense {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Employee employee;
    private BigDecimal netAmountPaid;
    private String paymentReference;
    // category will always be LABOR_PERSONNEL
}