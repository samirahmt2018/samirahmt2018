package com.gundan.terragold.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record InventoryPurchaseRequest(
        @NotNull Long itemTypeId,

        @NotNull @Positive BigDecimal quantity,

        @NotNull @Positive BigDecimal unitCost,

        @NotBlank String supplier,

        @NotNull Long purchaserId                     // who bought it (agent)
) {}