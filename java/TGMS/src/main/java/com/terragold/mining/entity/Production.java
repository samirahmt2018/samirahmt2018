package com.terragold.mining.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Production {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private BigDecimal quantity; // Grams

    private BigDecimal gradePurity; // Optional

    private String sourceBatchId;

    private String operatorId;
}