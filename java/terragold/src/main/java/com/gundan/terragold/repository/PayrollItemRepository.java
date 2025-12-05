package com.gundan.terragold.repository;


import com.gundan.terragold.entity.Machine;
import com.gundan.terragold.entity.PayrollItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface PayrollItemRepository extends
        JpaRepository<PayrollItem, Long>,
        JpaSpecificationExecutor<PayrollItem> {
    List<PayrollItem> findByPayrollRunId(Long runId);
}