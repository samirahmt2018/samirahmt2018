package com.gundan.terragold.repository;

import com.gundan.terragold.entity.EmployeeAdvanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeAdvanceTransactionRepository extends JpaRepository<EmployeeAdvanceTransaction, Long> {
    List<EmployeeAdvanceTransaction> findByAccountIdOrderByTransactionDateDesc(Long accountId);

    List<EmployeeAdvanceTransaction> findByTransactionDateBetween(LocalDate from, LocalDate to);

    @Query("SELECT COALESCE(SUM(CASE WHEN t.type = 'TOP_UP' THEN t.amount ELSE -t.amount END), 0) " +
            "FROM EmployeeAdvanceTransaction t WHERE t.account.employee.id = :employeeId")
    BigDecimal getCurrentBalanceByEmployeeId(Long employeeId);
}
