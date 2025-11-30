package com.gundan.terragold.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyLaborDto(
        Long id,
        LocalDate date,
        String fullName,
        String role,
        BigDecimal totalPaid,
        EmployeeDto paidBy       // "Office" or "Purchaser: Kidane"

) {}