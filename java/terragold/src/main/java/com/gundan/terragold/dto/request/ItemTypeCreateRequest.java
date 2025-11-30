package com.gundan.terragold.dto.request;


import com.gundan.terragold.enums.UnitOfMeasurement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemTypeCreateRequest(
        @NotNull
        @NotBlank(message = "Name is required")
        String name,
        @NotNull
        @NotBlank(message = "Asset Id is required")
        UnitOfMeasurement unitOfMeasurement,
        String Description
) {}
