package com.gundan.terragold.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.YearMonth;

@Entity
@Table(name = "payroll_inputs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /**
     * YYYY-MM format payroll period.
     */
    @Column(nullable = false)
    private YearMonth period;

    /**
     * INPUT FIELDS (Editable before payroll run)
     */
    @Column(nullable = false)
    @Builder.Default
    private BigDecimal overtimeHours = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal overtimeRate = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal bonusAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal deduction = BigDecimal.ZERO;

    /**
     * AUDIT FIELDS
     */
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }


}
