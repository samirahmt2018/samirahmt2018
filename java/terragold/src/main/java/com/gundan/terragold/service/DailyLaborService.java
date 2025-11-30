package com.gundan.terragold.service;

import com.gundan.terragold.entity.DailyLaborPayment;
import com.gundan.terragold.entity.Employee;
import com.gundan.terragold.entity.EmployeeAdvanceTransaction;
import com.gundan.terragold.repository.DailyLaborPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyLaborService {

    private final DailyLaborPaymentRepository dailyLaborRepo;
    private final AdvanceAccountService advanceService;

    /**
     * Pays daily labor from the office (cash/normal payment, no advance).
     *
     * @param fullName        laborer full name
     * @param role            laborer role
     * @param totalPaid       amount paid
     * @param paidByEmployeeId employee ID who paid
     * @param notes           notes for payment
     * @return persisted DailyLaborPayment
     */
    @Transactional
    public DailyLaborPayment payDailyLaborFromOffice(String fullName,
                                                     String role,
                                                     BigDecimal totalPaid,
                                                     Long paidByEmployeeId,
                                                     String notes) {
        Employee paidBy = Employee.builder().id(paidByEmployeeId).build();

        DailyLaborPayment payment = DailyLaborPayment.builder()
                .paymentDate(LocalDate.now())
                .fullName(fullName)
                .role(role)
                .totalPaid(totalPaid != null ? totalPaid : BigDecimal.ZERO)
                .paidBy(paidBy)
                .notes(notes)
                .build();

        return dailyLaborRepo.save(payment);
    }

    /**
     * Pays daily labor from purchaser's advance account.
     *
     * @param purchaserEmployeeId purchaser employee ID
     * @param fullName            laborer full name
     * @param role                laborer role
     * @param totalPaid           amount to pay
     * @param notes               notes for the payment
     * @return persisted DailyLaborPayment
     */
    @Transactional
    public DailyLaborPayment payDailyLaborFromAdvance(Long purchaserEmployeeId,
                                                      String fullName,
                                                      String role,
                                                      BigDecimal totalPaid,
                                                      String notes) {
        if (totalPaid == null) totalPaid = BigDecimal.ZERO;

        // Deduct from purchaser's advance account
        EmployeeAdvanceTransaction tx = advanceService.spend(
                purchaserEmployeeId,
                totalPaid,
                "Daily labor: " + fullName + " (" + role + ")",
                null
        );

        // Record the payment
        DailyLaborPayment payment = DailyLaborPayment.builder()
                .paymentDate(LocalDate.now())
                .fullName(fullName)
                .role(role)
                .totalPaid(totalPaid)
                .advanceTransaction(tx)
                .notes(notes)
                .build();

        return dailyLaborRepo.save(payment);
    }

    /**
     * Calculate total labor cost for today.
     *
     * @return total paid today
     */
    public BigDecimal getTodayLaborCost() {
        BigDecimal total = dailyLaborRepo.sumTotalPaidByDate(LocalDate.now());
        return total != null ? total : BigDecimal.ZERO;
    }
}
