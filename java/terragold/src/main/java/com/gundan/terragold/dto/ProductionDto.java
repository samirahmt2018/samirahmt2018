package com.gundan.terragold.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record ProductionDto(
        Long id,
        LocalDate productionDate,
        BigDecimal quantityGrams,
        String operatorName,
        BigDecimal gradePurity, String sourceBatchId,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {}