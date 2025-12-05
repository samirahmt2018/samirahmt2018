package com.gundan.terragold.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record MachineLogbookCreateRequest (
        @NotNull
        Long machineId,
    @NotNull
    LocalDate logDate,
    @NotNull
    Long operatorId,

    @NotNull
    LocalTime startTime,
    BigDecimal startHourMeter,
    @NotNull
    LocalTime endTime,
    BigDecimal endHourMeter,
    Integer repetitions){}

