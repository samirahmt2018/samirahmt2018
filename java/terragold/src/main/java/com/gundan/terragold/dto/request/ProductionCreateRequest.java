package com.gundan.terragold.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductionCreateRequest {
    @NotNull(message = "Production date is required")
    private LocalDate productionDate;
    @NotNull(message = "Quantity in grams is required")
    private BigDecimal quantityGrams;
    @NotNull(message = "Grade purity is required")
    private BigDecimal gradePurity;
    @NotNull(message = "Source batch ID is required")
    private String sourceBatchId;
    @NotNull(message = "Operator ID is required")
    private Long operatorId;
}
