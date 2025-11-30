package com.gundan.terragold.entity;

import com.gundan.terragold.enums.EmployeeRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private BigDecimal monthlySalary;
    private String notes; // Kept

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // One-to-one advance account
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmployeeAdvanceAccount advanceAccount;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        hireDate = hireDate == null ? LocalDate.now() : hireDate;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}