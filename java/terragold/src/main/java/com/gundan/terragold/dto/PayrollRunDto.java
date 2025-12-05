package com.gundan.terragold.dto;


import com.gundan.terragold.enums.PayrollStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;


public record PayrollRunDto (
    Long id,
    String payrollMonth,
    PayrollStatus status,
    List<PayrollItemDto> items
){
}