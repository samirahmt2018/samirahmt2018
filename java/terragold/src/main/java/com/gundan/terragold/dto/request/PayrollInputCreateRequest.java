package com.gundan.terragold.dto.request;

import java.math.BigDecimal;
import java.time.YearMonth;
import lombok.Data;

@Data
public class PayrollInputCreateRequest {
    private Long employeeId;
    private YearMonth period;
    private BigDecimal overtimeHours;
    private BigDecimal overtimeRate;
    private BigDecimal bonus;
    private BigDecimal deduction;
}
