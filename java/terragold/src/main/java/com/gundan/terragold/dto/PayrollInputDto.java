package com.gundan.terragold.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;


public record PayrollInputDto(
        Long employeeId,
        Long id,
        YearMonth period,
        BigDecimal overtimeHours,
        BigDecimal overtimeRate,
        BigDecimal bonus,
        BigDecimal deduction
        ) {
}
