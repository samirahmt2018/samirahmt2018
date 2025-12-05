package com.gundan.terragold.repository;

import com.gundan.terragold.entity.Machine;
import com.gundan.terragold.entity.MachineLogbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface MachineLogbookRepository extends JpaRepository<MachineLogbook, Long>, JpaSpecificationExecutor<MachineLogbook> {
    List<MachineLogbook> findByMachineIdAndLogDate(Long machineId, LocalDate logDate);
    List<MachineLogbook> findByOperatorIdAndLogDate(Long operatorId, LocalDate logDate);
    List<MachineLogbook> findByLogDateBetween(LocalDate start, LocalDate end);
}
