package com.gundan.terragold.entity;


import com.gundan.terragold.enums.PayrollStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "payroll_runs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PayrollRun {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Integer periodYear;
    private Integer periodMonth;
    private String payrollMonth; // YYYY-MM
    private boolean applyTax;     // NEW
    private boolean applyPension;

    @Enumerated(EnumType.STRING)
    private PayrollStatus status = PayrollStatus.PENDING;


    private OffsetDateTime generatedAt;
    private OffsetDateTime approvedAt;
    private Long approvedBy;
    private OffsetDateTime paidAt;


    private String notes;


    @OneToMany(mappedBy = "payrollRun", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PayrollItem> items = new ArrayList<>();
    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}