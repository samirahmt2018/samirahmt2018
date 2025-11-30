package com.terragold.mining.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.terragold.mining.enums.ExpenseCategory;
@Entity
@Data
public class InventoryPurchase {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@NotNull
private LocalDate date;

@NotNull
private String supplierName;

@NotNull
private String itemName;

@NotNull
@Enumerated(EnumType.STRING)
private ExpenseCategory itemCategory;

@NotNull
private BigDecimal quantityPurchased;

@NotNull
private BigDecimal unitCost; // ETB

@NotNull
private BigDecimal totalCost;

private String receiptInvoiceNumber;
}