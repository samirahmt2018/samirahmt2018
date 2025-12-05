package com.gundan.terragold.dto;


import com.gundan.terragold.enums.PayrollItemStatus;
import lombok.Data;

import java.math.BigDecimal;


public record PayrollItemDto(
        Long id,
        Long employeeId,
        String employeeName,
        BigDecimal baseSalary,
        BigDecimal housingAllowance,
        BigDecimal transportAllowance,
        BigDecimal otherFixedAllowance,
        BigDecimal overtimeAmount,
        BigDecimal bonusAmount,
        BigDecimal taxAmount,
        BigDecimal pensionEmployee,
        BigDecimal pensionEmployer,
        BigDecimal deduction,
        BigDecimal netPay,
        PayrollItemStatus status) {
}