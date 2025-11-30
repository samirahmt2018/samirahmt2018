package com.gundan.terragold.dto;

import com.gundan.terragold.enums.ExpenseCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GeneralExpenseDto(
        Long id,
        LocalDate date,
        ExpenseCategory category,
        String description,
        BigDecimal amount,
        String paidFrom
) {}