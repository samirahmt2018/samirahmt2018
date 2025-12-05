package com.gundan.terragold.repository;

import com.gundan.terragold.entity.PayrollRun;
import com.gundan.terragold.entity.SalaryPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SalaryPaymentRepository extends
        JpaRepository<SalaryPayment, Long>,
        JpaSpecificationExecutor<SalaryPayment>
{
    List<SalaryPayment> findByPayrollMonth(String month);
    List<SalaryPayment> findByPayrollRunId(Long payrollRunId);
}
