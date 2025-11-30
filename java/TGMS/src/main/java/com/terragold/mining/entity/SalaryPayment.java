package com.terragold.mining.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import com.terragold.mining.enums.ExpenseCategory;
@Entity
@Data
public class SalaryPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String employeeName;

    private String role;

    private BigDecimal netMonthlySalary; // ETB

    @NotNull
    private LocalDate dateOfPayment;

    @NotNull
    private BigDecimal netAmountPaid; // ETB

    private String paymentReference;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category = ExpenseCategory.LABOR_PERSONNEL; // Default
}