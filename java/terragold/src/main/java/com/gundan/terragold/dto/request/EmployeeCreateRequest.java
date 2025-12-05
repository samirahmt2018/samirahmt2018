package com.gundan.terragold.dto.request;

import com.gundan.terragold.enums.EmployeeRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeCreateRequest(

        @NotBlank(message = "Employee code is required")
        String employeeCode,

        @NotBlank(message = "Full name is required")
        String fullName,

        String phoneNumber,

        EmployeeRole role,

        String department,

        LocalDate hireDate,

        @NotNull(message = "Monthly salary is required") // VALIDATION ADDED
        BigDecimal baseSalary,

        @NotNull(message = "Housing allowance is required")
        BigDecimal housingAllowance,
        @NotNull(message = "Transport allowance is required")
        BigDecimal transportAllowance,
        @NotNull(message = "Other fixed allowance is required")
        BigDecimal otherFixedAllowance,

        String notes
) {}