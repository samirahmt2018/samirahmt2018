package com.gundan.terragold.service;

import com.gundan.terragold.entity.*;
import com.gundan.terragold.enums.ExpenseCategory;
import com.gundan.terragold.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseService {

    private final GeneralExpenseRepository expenseRepo;
    private final AdvanceAccountService advanceService;

    @Transactional
    public GeneralExpense recordOfficeExpense(ExpenseCategory category,
                                              String description,
                                              BigDecimal amount,
                                              Long paidByEmployeeId) {
        Employee paidBy= Employee.builder().id(paidByEmployeeId).build();
        GeneralExpense exp = GeneralExpense.builder()
                .expenseDate(LocalDate.now())
                .category(category)
                .description(description)
                .amount(amount)
                .paidBy(paidBy)
                .build();
        return expenseRepo.save(exp);
    }

    @Transactional
    public GeneralExpense recordExpenseFromAdvance(Long purchaserEmployeeId,
                                                   ExpenseCategory category,
                                                   String description,
                                                   BigDecimal amount,
                                                   String receiptRef) {
        EmployeeAdvanceTransaction tx = advanceService.spend(purchaserEmployeeId, amount, description, receiptRef);
        Employee purchaser= Employee.builder().id(purchaserEmployeeId).build();
        GeneralExpense exp = GeneralExpense.builder()
                .expenseDate(LocalDate.now())
                .category(category)
                .description(description)
                .amount(amount)
                .paidBy(purchaser)
                .advanceTransaction(tx)
                .build();
        return expenseRepo.save(exp);
    }
}