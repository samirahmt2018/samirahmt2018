package com.gundan.terragold.dto;

import java.math.BigDecimal;

public record AdvanceAccountDto(
        Long employeeId,
        String employeeName,
        String employeeCode,
        BigDecimal currentBalance
) {}
