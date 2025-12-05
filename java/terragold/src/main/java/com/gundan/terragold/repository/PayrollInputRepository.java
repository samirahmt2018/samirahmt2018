package com.gundan.terragold.repository;

import com.gundan.terragold.entity.Machine;
import com.gundan.terragold.entity.PayrollInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface PayrollInputRepository extends JpaRepository<PayrollInput, Long>, JpaSpecificationExecutor<PayrollInput> {

    Optional<PayrollInput> findByEmployeeIdAndPeriod(Long employeeId, YearMonth period);

    List<PayrollInput> findAllByPeriod(YearMonth period);

    List<PayrollInput> findAllByEmployeeIdInAndPeriod(List<Long> employeeIds, YearMonth period);

    boolean existsByPeriod(YearMonth period);

    void deleteByPeriod(YearMonth period);
}
