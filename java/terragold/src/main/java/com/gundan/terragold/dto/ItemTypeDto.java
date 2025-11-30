package com.gundan.terragold.dto;

import com.gundan.terragold.enums.UnitOfMeasurement;

import java.time.LocalDateTime;

public record ItemTypeDto(
        Long id,
        String name,
        UnitOfMeasurement unitOfMeasurement,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}