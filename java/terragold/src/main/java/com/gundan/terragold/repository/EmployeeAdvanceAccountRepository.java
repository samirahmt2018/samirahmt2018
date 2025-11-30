package com.gundan.terragold.repository;

import com.gundan.terragold.entity.EmployeeAdvanceAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeAdvanceAccountRepository extends JpaRepository<EmployeeAdvanceAccount, Long> {
    Optional<EmployeeAdvanceAccount> findByEmployeeId(Long employeeId);

    // getting findByCurrentBalanceLessThan
    List<EmployeeAdvanceAccount> findByCurrentBalanceLessThan(java.math.BigDecimal amount);
}