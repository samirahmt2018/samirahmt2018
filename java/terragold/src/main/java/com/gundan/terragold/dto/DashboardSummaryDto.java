package com.gundan.terragold.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardSummaryDto(
        BigDecimal goldThisMonth,
        BigDecimal laborToday,
        int lowBalanceAgents,
        int activePurchasers,
        List<AdvanceAccountDto> agentBalances
) {}