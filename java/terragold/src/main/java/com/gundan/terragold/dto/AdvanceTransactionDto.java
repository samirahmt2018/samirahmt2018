package com.gundan.terragold.dto;

import com.gundan.terragold.enums.AdvanceTransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AdvanceTransactionDto(
        Long id,
        LocalDate transactionDate,
        AdvanceTransactionType type,           // TOP_UP or EXPENSE
        BigDecimal amount,
        String description,
        String receiptRef,
        BigDecimal balanceAfter
) {}