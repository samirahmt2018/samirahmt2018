package com.terragold.mining.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.terragold.mining.enums.ExpenseCategory;

@Entity
@Data
public class ExpenseRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private String vendor;

    @NotNull
    private String itemDescription;

    @NotNull
    private BigDecimal cost; // ETB

    @NotNull
    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

    private String receiptAttachment; // URL/path
}