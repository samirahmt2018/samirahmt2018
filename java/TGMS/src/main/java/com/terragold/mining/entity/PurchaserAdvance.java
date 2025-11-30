package com.terragold.mining.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class PurchaserAdvance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String purchaserName;

    @NotNull
    private LocalDate date;

    @NotNull
    private BigDecimal amountAdvanced; // ETB

    private String purpose;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ExpenseRecord> expenses;

    // Computed balance
    @Transient
    public BigDecimal getBalance() {
        BigDecimal totalSpent = expenses.stream()
                .map(ExpenseRecord::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return amountAdvanced.subtract(totalSpent);
    }
}