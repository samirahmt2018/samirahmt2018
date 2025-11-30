// 2. SpendRequest.java
package com.gundan.terragold.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record SpendRequest(
        @NotNull @Positive BigDecimal amount,

        @NotBlank @Size(max = 255) String description,

        @Size(max = 100) String receiptRef          // optional
) {}