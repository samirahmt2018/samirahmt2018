// 1. TopUpRequest.java
package com.gundan.terragold.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TopUpRequest(
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotBlank(message = "Description is required")
        @Size(max = 255)
        String description
) {}