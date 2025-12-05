package com.gundan.terragold.entity;

import com.gundan.terragold.enums.EmployeeRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String employeeCode;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeRole role;

    private String department;

    private LocalDate hireDate;
    private LocalDate terminationDate;

    // SALARY FIELDS (MUST NEVER BE NULL)
    @Column(nullable = false)
    private BigDecimal baseSalary = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal housingAllowance = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal transportAllowance = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal otherFixedAllowance = BigDecimal.ZERO;

    private String notes;
    @Column(updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt = OffsetDateTime.now();
    // One-to-one advance account
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmployeeAdvanceAccount advanceAccount;

    @PrePersist
    protected void onCreate() {

        if (hireDate == null) {
            hireDate = LocalDate.now();
        }

        // Salary fields: ensure no nulls
        baseSalary = baseSalary == null ? BigDecimal.ZERO : baseSalary;
        housingAllowance = housingAllowance == null ? BigDecimal.ZERO : housingAllowance;
        transportAllowance = transportAllowance == null ? BigDecimal.ZERO : transportAllowance;
        otherFixedAllowance = otherFixedAllowance == null ? BigDecimal.ZERO : otherFixedAllowance;
    }

 
}
