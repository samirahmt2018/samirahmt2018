package com.gundan.terragold.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.gundan.terragold.enums.AdvanceTransactionType;

@Entity
@Table(name = "employee_advance_transactions")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class EmployeeAdvanceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private EmployeeAdvanceAccount account;

    @NotNull
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AdvanceTransactionType type;   // TOP_UP or EXPENSE

    @NotNull
    @Column(precision = 18, scale = 2)
    private BigDecimal amount;             // always positive

    private String description;            // “Fuel in Humera”, “Paid 25 daily laborers”, etc.

    private String receiptRef;             // photo, invoice number

    // Optional – who recorded this transaction
    @ManyToOne
    private AppUser recordedBy;


    // These two are calculated and saved for fast reporting
    @Column(precision = 18, scale = 2)
    private BigDecimal balanceBefore;

    @Column(precision = 18, scale = 2)
    private BigDecimal balanceAfter;


    @Column(updatable = false)
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;
    @PrePersist
    void calculateBalances() {
        this.balanceBefore = account.getCurrentBalance();
        if (type == AdvanceTransactionType.TOP_UP) {
            this.balanceAfter = balanceBefore.add(amount);
        } else {
            this.balanceAfter = balanceBefore.subtract(amount);
        }
        this.createdAt = LocalDateTime.now();

        // Update the running balance in the account
        account.setCurrentBalance(this.balanceAfter);
    }
}


