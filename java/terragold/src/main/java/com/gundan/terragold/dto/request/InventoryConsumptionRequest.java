package com.gundan.terragold.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record InventoryConsumptionRequest(
        @NotNull Long itemTypeId,

        @NotNull @Positive BigDecimal quantity,

        @NotNull Long machineId,

        @NotNull Long issuedById                      // who issued it
) {}