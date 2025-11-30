package com.gundan.terragold.service;

import com.gundan.terragold.entity.*;
import com.gundan.terragold.enums.AdvanceTransactionType;
import com.gundan.terragold.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvanceAccountService {

    private final EmployeeAdvanceAccountRepository accountRepo;
    private final EmployeeAdvanceTransactionRepository transactionRepo;
    private final EmployeeRepository employeeRepo;

    public EmployeeAdvanceAccount getAccountByEmployeeId(Long employeeId) {
        return accountRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Advance account not found for employee " + employeeId));
    }

    @Transactional
    public EmployeeAdvanceTransaction topUp(Long employeeId, BigDecimal amount, String description, Long recordedById) {
        EmployeeAdvanceAccount account = getAccountByEmployeeId(employeeId);

        EmployeeAdvanceTransaction tx = EmployeeAdvanceTransaction.builder()
                .account(account)
                .transactionDate(LocalDate.now())
                .type(AdvanceTransactionType.TOP_UP)
                .amount(amount)
                .description(description)
                .recordedBy(recordedById != null ? AppUser.builder().id(recordedById).build() : null)
                .balanceBefore(account.getCurrentBalance())
                .balanceAfter(account.getCurrentBalance().add(amount))
                .build();

        account.setCurrentBalance(tx.getBalanceAfter());
        transactionRepo.save(tx);
        return tx;
    }

    @Transactional
    public EmployeeAdvanceTransaction spend(Long employeeId,
                                            BigDecimal amount,
                                            String description,
                                            String receiptRef) {
        EmployeeAdvanceAccount account = getAccountByEmployeeId(employeeId);
        BigDecimal newBalance = account.getCurrentBalance().subtract(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance. Current: " + account.getCurrentBalance());
        }

        EmployeeAdvanceTransaction tx = EmployeeAdvanceTransaction.builder()
                .account(account)
                .transactionDate(LocalDate.now())
                .type(AdvanceTransactionType.EXPENSE)
                .amount(amount)
                .description(description)
                .receiptRef(receiptRef)
                .balanceBefore(account.getCurrentBalance())
                .balanceAfter(newBalance)
                .build();

        account.setCurrentBalance(newBalance);
        transactionRepo.save(tx);
        return tx;
    }

    public List<EmployeeAdvanceTransaction> getTransactionHistory(Long employeeId) {
        EmployeeAdvanceAccount account = getAccountByEmployeeId(employeeId);
        return transactionRepo.findByAccountIdOrderByTransactionDateDesc(account.getId());
    }
    // getting getAccountsWithBalanceBelow
    public List<EmployeeAdvanceAccount> getAccountsWithBalanceBelow(BigDecimal threshold) {
        return accountRepo.findByCurrentBalanceLessThan(threshold);
    }
}