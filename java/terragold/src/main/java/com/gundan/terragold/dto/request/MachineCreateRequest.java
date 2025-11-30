package com.gundan.terragold.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MachineCreateRequest(
        @NotNull
        @NotBlank(message = "Machine Name is required")
        String machineType,
        @NotNull
        @NotBlank(message = "Asset Id is required")
        String assetMachineId,

        String Description

) {}
