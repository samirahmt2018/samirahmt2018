package com.gundan.terragold.repository;

import com.gundan.terragold.entity.DailyLaborPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DailyLaborPaymentRepository extends JpaRepository<DailyLaborPayment, Long> {

    // Find all payments in a date range
    List<DailyLaborPayment> findByPaymentDateBetween(LocalDate from, LocalDate to);

    // Sum of totalPaid in a date range
    @Query("SELECT SUM(d.totalPaid) FROM DailyLaborPayment d WHERE d.paymentDate BETWEEN :from AND :to")
    BigDecimal getTotalLaborCostBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    // Sum of totalPaid for a specific date
    @Query("SELECT SUM(d.totalPaid) FROM DailyLaborPayment d WHERE d.paymentDate = :date")
    BigDecimal sumTotalPaidByDate(@Param("date") LocalDate date);

    // Find all payments linked to a specific advance transaction
    List<DailyLaborPayment> findByAdvanceTransactionId(Long transactionId);
}
