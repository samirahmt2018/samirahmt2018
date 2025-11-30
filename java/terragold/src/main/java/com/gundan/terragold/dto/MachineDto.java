package com.gundan.terragold.dto;

import java.time.LocalDateTime;

public record MachineDto(
        Long id,
       String machineName,
        String assetMachineId,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}