package com.gundan.terragold.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
public record ProductionDto(
        Long id,
        LocalDate date,
        BigDecimal grams,
        BigDecimal purity,
        String operatorName,
        String batchId
) {}