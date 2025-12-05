package com.gundan.terragold.service;

import com.gundan.terragold.dto.*;
import com.gundan.terragold.dto.request.MachineLogbookCreateRequest;
import com.gundan.terragold.dto.request.MachineLogbookUpdateRequest;
import com.gundan.terragold.entity.*;
import com.gundan.terragold.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MachineLogbookService {

    private final MachineLogbookRepository logbookRepository;
    private final MachineRepository machineRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public MachineLogbookDto createLog(MachineLogbookCreateRequest request) {
        Machine machine = machineRepository.findById(request.machineId())
                .orElseThrow(() -> new RuntimeException("Machine not found"));
        Employee operator = employeeRepository.findById(request.operatorId())
                .orElseThrow(() -> new RuntimeException("Operator not found"));

        BigDecimal totalHours = computeTotalHours(request.startHourMeter(), request.endHourMeter());

        MachineLogbook log = MachineLogbook.builder()
                .machine(machine)
                .operator(operator)
                .logDate(request.logDate())
                .startTime(request.startTime())
                .startHourMeter(request.startHourMeter())
                .endTime(request.endTime())
                .endHourMeter(request.endHourMeter())
                .totalHours(totalHours)
                .repetitions(request.repetitions())
                .build();
        MachineLogbook saved = logbookRepository.save(log);
        return toDto(saved);
    }

    @Transactional
    public MachineLogbookDto updateLog(Long id, MachineLogbookUpdateRequest request) {
        MachineLogbook log = logbookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found"));

        if (request.getLogDate() != null) log.setLogDate(request.getLogDate());
        if (request.getOperatorId() != null) {
            Employee operator = employeeRepository.findById(request.getOperatorId())
                    .orElseThrow(() -> new RuntimeException("Operator not found"));
            log.setOperator(operator);
        }
        if (request.getStartTime() != null) log.setStartTime(request.getStartTime());
        if (request.getStartHourMeter() != null) log.setStartHourMeter(request.getStartHourMeter());
        if (request.getEndTime() != null) log.setEndTime(request.getEndTime());
        if (request.getEndHourMeter() != null) log.setEndHourMeter(request.getEndHourMeter());
        if (request.getRepetitions() != null) log.setRepetitions(request.getRepetitions());

        // Recompute total hours if meters are updated
        if (log.getStartHourMeter() != null && log.getEndHourMeter() != null) {
            log.setTotalHours(computeTotalHours(log.getStartHourMeter(), log.getEndHourMeter()));
        }

        MachineLogbook updated = logbookRepository.save(log);
        return toDto(updated);
    }

    @Transactional(readOnly = true)
    public List<MachineLogbookDto> listLogs() {
        return logbookRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MachineLogbookDto getLog(Long id) {
        MachineLogbook log = logbookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found"));
        return toDto(log);
    }

    @Transactional
    public void deleteLog(Long id) {
        logbookRepository.deleteById(id);
    }

    private BigDecimal computeTotalHours(BigDecimal startMeter, BigDecimal endMeter) {
        if (startMeter == null || endMeter == null) return BigDecimal.ZERO;
        return endMeter.subtract(startMeter);
    }

    private MachineLogbookDto toDto(MachineLogbook log) {
        return new MachineLogbookDto(
                log.getId(),
                log.getMachine().getId(),
                log.getMachine().getMachineType(),
                log.getOperator() != null ? log.getOperator().getFullName() : null,
                log.getLogDate(),
                log.getStartTime(),
                log.getStartHourMeter(),
                log.getEndTime(),
                log.getEndHourMeter(), log.getTotalHours(),
                log.getRepetitions());
    }
}
