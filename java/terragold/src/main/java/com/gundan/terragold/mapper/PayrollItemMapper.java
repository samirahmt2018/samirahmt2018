package com.gundan.terragold.mapper;

import com.gundan.terragold.dto.PayrollItemDto;
import com.gundan.terragold.entity.PayrollItem;

import java.util.Collections;

public class PayrollItemMapper {

    public static PayrollItemDto toDto(PayrollItem m) {
        return new PayrollItemDto(
                m.getId(),
                m.getEmployee().getId(),
                m.getEmployee().getFullName(),
                m.getBaseSalary(),
                m.getHousingAllowance(),
                m.getTransportAllowance(),
                m.getOtherFixedAllowance(),
                m.getOvertimeAmount(),
                m.getBonusAmount(),
                m.getTaxAmount(),
                m.getPensionEmployee(),
                m.getPensionEmployer(),
                m.getDeduction(),
                m.getNetPay(),
                m.getStatus()
        );
    }


}