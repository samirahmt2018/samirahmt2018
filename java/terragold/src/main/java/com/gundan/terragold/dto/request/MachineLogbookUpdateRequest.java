package com.gundan.terragold.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class MachineLogbookUpdateRequest {
    private LocalDate logDate;
    private Long operatorId;
    private LocalTime startTime;
    private BigDecimal startHourMeter;
    private LocalTime endTime;
    private BigDecimal endHourMeter;
    private Integer repetitions;
}
