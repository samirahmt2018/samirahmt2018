package com.gundan.terragold.mapper;

import com.gundan.terragold.dto.AdvanceAccountDto;
import com.gundan.terragold.dto.AdvanceTransactionDto;
import com.gundan.terragold.entity.EmployeeAdvanceAccount;
import com.gundan.terragold.entity.EmployeeAdvanceTransaction;

public class AdvanceTransactionMapper {
    public static AdvanceTransactionDto toDto(EmployeeAdvanceTransaction at) {
        return new AdvanceTransactionDto(
                at.getId(),
                at.getTransactionDate(),
                at.getType(),
                at.getAmount(),
                at.getDescription(),
                at.getReceiptRef(),
                at.getBalanceAfter()
        );
    };


}
