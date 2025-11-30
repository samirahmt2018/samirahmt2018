package com.terragold.mining.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.terragold.mining.enums.ExpenseCategory;
@Entity
@Data
public class InventoryConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate dateOfIssue;

    @NotNull
    private String itemName;

    @NotNull
    private BigDecimal quantityIssued;

    @NotNull
    private String assetMachineId;

    private String machineType;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;
}