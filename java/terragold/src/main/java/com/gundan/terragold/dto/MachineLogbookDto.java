package com.gundan.terragold.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


public record MachineLogbookDto (
    Long id,
    Long machineId,
    String machineType,
    String operatorName,
    LocalDate logDate,
    LocalTime startTime,
    BigDecimal startHourMeter,
    LocalTime endTime,
    BigDecimal endHourMeter,
    BigDecimal totalHours,
    Integer repetitions
){}
