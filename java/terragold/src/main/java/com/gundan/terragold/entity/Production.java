package com.gundan.terragold.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity

@Table(name = "productions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Production {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate productionDate;

    @NotNull
    private BigDecimal quantityGrams; // Grams

    private BigDecimal gradePurity; // Optional

    private String sourceBatchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id")
    private Employee operator;
}