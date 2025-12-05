package com.gundan.terragold.dto.request;


import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductionUpdateRequest {
    private LocalDate productionDate;
    private BigDecimal quantityGrams;
    private BigDecimal gradePurity;
    private String sourceBatchId;
    private Long operatorId;
}
