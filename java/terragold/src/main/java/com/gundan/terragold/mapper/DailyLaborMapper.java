package com.gundan.terragold.mapper;

import com.gundan.terragold.dto.DailyLaborDto;
import com.gundan.terragold.entity.DailyLaborPayment;

public class DailyLaborMapper {
    public static DailyLaborDto toLaborDto(DailyLaborPayment p) {
        return new DailyLaborDto(
                p.getId(),
                p.getPaymentDate(),
                p.getFullName(),
                p.getRole(),
                p.getTotalPaid(),
                EmployeeMapper.toDto(p.getPaidBy())
        );
    }
}
