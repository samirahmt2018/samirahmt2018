package com.gundan.terragold.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record MachineDto(
        Long id,
       String machineName,
        String assetMachineId,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}