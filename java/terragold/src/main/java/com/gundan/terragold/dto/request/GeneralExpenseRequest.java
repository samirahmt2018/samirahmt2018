// 4. GeneralExpenseRequest.java
package com.gundan.terragold.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record GeneralExpenseRequest(
        @NotBlank String category,                   // FUEL, RENT, FOOD, etc.

        @NotBlank String description,

        @NotNull @Positive BigDecimal amount,

        @Size(max = 100) String receiptRef
) {}