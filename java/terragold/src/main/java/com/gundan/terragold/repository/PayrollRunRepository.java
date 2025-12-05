package com.gundan.terragold.repository;


import com.gundan.terragold.entity.Machine;
import com.gundan.terragold.entity.PayrollRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface PayrollRunRepository extends
        JpaRepository<PayrollRun, Long>,
        JpaSpecificationExecutor<PayrollRun>
{
    Optional<PayrollRun> findByPeriodYearAndPeriodMonth(Integer year, Integer month);
    Optional<PayrollRun> findByPayrollMonth(String payrollMonth);
}