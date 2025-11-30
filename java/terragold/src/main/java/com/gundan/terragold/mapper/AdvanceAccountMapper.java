package com.gundan.terragold.mapper;

import com.gundan.terragold.dto.AdvanceAccountDto;
import com.gundan.terragold.entity.EmployeeAdvanceAccount;

public class AdvanceAccountMapper {
    public static AdvanceAccountDto toDto(EmployeeAdvanceAccount ac) {
        return new AdvanceAccountDto(
                ac.getEmployee().getId(),
                ac.getEmployee().getFullName(),
                ac.getEmployee().getEmployeeCode(),
                ac.getCurrentBalance()
        );
    }
}
