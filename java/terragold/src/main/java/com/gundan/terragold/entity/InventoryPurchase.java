package com.gundan.terragold.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.gundan.terragold.enums.ExpenseCategory;

@Entity
@Table(name = "inventory_purchases")
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class InventoryPurchase extends AbstractExpense {
    private String supplierName;
    private String itemName;

    @ManyToOne
    private ItemType itemType; // optional, if you want strong link
    private LocalDate purchaseDate;
    private BigDecimal quantityPurchased;
    private BigDecimal unitCost;
    private String receiptInvoiceNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchased_by_employee_id")
    private Employee purchasedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advance_transaction_id")
    private EmployeeAdvanceTransaction advanceTransaction;   // ← links it to the agent’s balance

}
