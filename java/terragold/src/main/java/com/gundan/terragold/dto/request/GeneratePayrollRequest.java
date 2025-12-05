package com.gundan.terragold.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class GeneratePayrollRequest {
    @NotBlank
    @Pattern(regexp = "\\d{4}-\\d{2}",  message = "payrollMonth must follow YYYY-MM format (example: 2025-01)"
    )
    private String payrollMonth; // YYYY-MM
    private boolean applyTax = true;     // default: true
    private boolean applyPension = true; // default: true
}