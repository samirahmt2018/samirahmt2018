package com.gundan.terragold.entity;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.gundan.terragold.enums.ExpenseCategory;

@Entity
@Table(name = "daily_labor_payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyLaborPayment extends AbstractExpense {

    @NotNull
    private LocalDate paymentDate;

    @NotNull
    private String fullName;                  // e.g. "Ato Gebre Tsadik"

    private String idNumber;                  // Ethiopian ID or just phone

    private String role;                      // Digger, Guard, Crusher, Carrier, etc.

    @NotNull
    private BigDecimal dailyRate;             // usually 300–800 ETB/day

    private Integer daysWorked;               // if paid weekly/monthly batch

    @NotNull
    private BigDecimal totalPaid;

   // paidby employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_by_employee_id")
    private Employee paidBy;

    private String notes;// who handed the cash

    private String phoneNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advance_transaction_id")
    private EmployeeAdvanceTransaction advanceTransaction;   // ← links it to the agent’s balance

}