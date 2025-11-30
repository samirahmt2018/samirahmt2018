// 3. DailyLaborRequest.java
package com.gundan.terragold.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record DailyLaborRequest(
        @NotBlank String fullName,

        @NotBlank String role,                       // Digger, Guard, Crusher...

        @NotNull @Positive BigDecimal totalPaid,

        String notes
) {}