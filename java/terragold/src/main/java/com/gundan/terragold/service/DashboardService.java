package com.gundan.terragold.service;

import com.gundan.terragold.entity.EmployeeAdvanceAccount;
import com.gundan.terragold.enums.EmployeeRole;
import com.gundan.terragold.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AdvanceAccountService advanceService;
    private final DailyLaborService dailyLaborService;
    private final ProductionService productionService;
    private final EmployeeRepository employeeRepo;

    /**
     * DTO for returning dashboard summary metrics.
     */
    public record DashboardSummary(
            BigDecimal totalGoldThisMonth,
            BigDecimal totalLaborCostToday,
            List<EmployeeAdvanceAccount> lowBalanceAgents,
            int activePurchasers
    ) {}

    /**
     * Get today's dashboard summary:
     * - Total gold produced this month
     * - Today's labor cost
     * - List of advance accounts with balance below 500,000
     * - Number of active purchasers
     */
    public DashboardSummary getTodaySummary() {
        BigDecimal totalGold = productionService.getTotalGoldThisMonth();
        BigDecimal laborCostToday = dailyLaborService.getTodayLaborCost();

        // Low balance alert (< 500k)
        List<EmployeeAdvanceAccount> lowBalanceAgents = advanceService.getAccountsWithBalanceBelow(new BigDecimal("500000"));

        // Active purchasers
        int activePurchasers = employeeRepo.findByRole(EmployeeRole.PURCHASER).size();

        return new DashboardSummary(
                totalGold != null ? totalGold : BigDecimal.ZERO,
                laborCostToday != null ? laborCostToday : BigDecimal.ZERO,
                lowBalanceAgents,
                activePurchasers
        );
    }
}
