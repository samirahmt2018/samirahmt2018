package com.gundan.terragold.dto;

import com.gundan.terragold.enums.UnitOfMeasurement;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record ItemTypeDto(
        Long id,
        String name,
        UnitOfMeasurement unitOfMeasurement,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}