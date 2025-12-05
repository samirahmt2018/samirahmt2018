package com.gundan.terragold.dto;

import com.gundan.terragold.enums.EmployeeRole;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record EmployeeDto(
        Long id,
        String employeeCode,
        String fullName,
        String phoneNumber,
        EmployeeRole role,
        String department,
        LocalDate hireDate,
        LocalDate terminationDate,
        BigDecimal baseSalary,
        BigDecimal housingAllowance,
        BigDecimal transportAllowance,
        BigDecimal otherFixedAllowance,
        String notes, // NEW FIELD ADDED
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}