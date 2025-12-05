package com.gundan.terragold.dto.request;

import java.math.BigDecimal;
import java.time.YearMonth;
import lombok.Data;

@Data
public class PayrollInputUpdateRequest {
    private BigDecimal overtimeHours;
    private BigDecimal overtimeRate;
    private BigDecimal bonus;
    private BigDecimal deduction;
}
