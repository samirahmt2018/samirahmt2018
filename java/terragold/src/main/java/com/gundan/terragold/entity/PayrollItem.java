package com.gundan.terragold.entity;


import com.gundan.terragold.enums.PayrollItemStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Entity
@Table(name = "payroll_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PayrollItem {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_run_id", nullable = false)
    private PayrollRun payrollRun;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;


    private BigDecimal baseSalary;
    private BigDecimal housingAllowance = BigDecimal.ZERO;
    private BigDecimal transportAllowance = BigDecimal.ZERO;
    private BigDecimal otherFixedAllowance = BigDecimal.ZERO;


    private BigDecimal overtimeAmount = BigDecimal.ZERO;
    private BigDecimal bonusAmount = BigDecimal.ZERO;

    private BigDecimal taxAmount = BigDecimal.ZERO;
    private BigDecimal pensionEmployee = BigDecimal.ZERO;
    private BigDecimal pensionEmployer = BigDecimal.ZERO;
    private BigDecimal deduction = BigDecimal.ZERO;
    private BigDecimal netPay = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    private PayrollItemStatus status = PayrollItemStatus.PENDING;

    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}