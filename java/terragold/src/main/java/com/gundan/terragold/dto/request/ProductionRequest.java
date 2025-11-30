package com.gundan.terragold.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductionRequest(
        @NotNull @Positive BigDecimal grams,

        @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal purity,  // optional

        Long operatorId,

        String batchId
) {}