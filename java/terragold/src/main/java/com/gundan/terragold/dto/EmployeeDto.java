package com.gundan.terragold.dto;

import com.gundan.terragold.enums.EmployeeRole;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeDto(
        Long id,
        String employeeCode,
        String fullName,
        String phoneNumber,
        EmployeeRole role,
        String department,
        LocalDate hireDate,
        LocalDate terminationDate,
        BigDecimal monthlySalary,
        String notes, // NEW FIELD ADDED
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}