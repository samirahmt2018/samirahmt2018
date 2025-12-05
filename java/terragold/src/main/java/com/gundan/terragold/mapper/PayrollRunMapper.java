package com.gundan.terragold.mapper;

import com.gundan.terragold.dto.PayrollRunDto;
import com.gundan.terragold.entity.PayrollRun;

import java.util.Collections;

public class PayrollRunMapper {

    public static PayrollRunDto toDto(PayrollRun m) {
        return new PayrollRunDto(
                m.getId(),
                m.getPayrollMonth(),
                m.getStatus(),
                m.getItems().stream()
                        .map(PayrollItemMapper::toDto) // Call the item mapper here
                        .toList()
        );
    }


}