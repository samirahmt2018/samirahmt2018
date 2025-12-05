package com.gundan.terragold.service;

import com.gundan.terragold.entity.Employee;
import com.gundan.terragold.entity.Production;
import com.gundan.terragold.repository.EmployeeRepository;
import com.gundan.terragold.repository.ProductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProductionRepository productionRepository;
    private final EmployeeRepository employeeRepository;

    // Today's Production: grams + pure gold
    public Map<String, BigDecimal> getTodayProduction() {
        LocalDate today = LocalDate.now();
        ZoneOffset zoneOffset = ZoneOffset.UTC; // adjust if you store OffsetDateTime in UTC

        OffsetDateTime start = today.atStartOfDay().atOffset(zoneOffset);
        OffsetDateTime end = today.plusDays(1).atStartOfDay().atOffset(zoneOffset);

        List<Production> list = productionRepository.findByCreatedAtBetween(start, end);

        BigDecimal totalGrams = list.stream()
                .map(Production::getQuantityGrams)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pureGold = list.stream()
                .map(p -> {
                    BigDecimal grams = p.getQuantityGrams() != null ? p.getQuantityGrams() : BigDecimal.ZERO;
                    BigDecimal purity = p.getGradePurity() != null ? p.getGradePurity() : BigDecimal.valueOf(100);
                    return grams.multiply(purity).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> result = new HashMap<>();
        result.put("grams", totalGrams.setScale(2, RoundingMode.HALF_UP));
        result.put("pureGold", pureGold.setScale(2, RoundingMode.HALF_UP));
        return result;
    }

    // Current Month Production
    public Map<String, BigDecimal> getCurrentMonthProduction() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate startOfNextMonth = now.plusMonths(1).withDayOfMonth(1);

        OffsetDateTime start = startOfMonth.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = startOfNextMonth.atStartOfDay().atOffset(ZoneOffset.UTC);

        List<Production> list = productionRepository.findByCreatedAtBetween(start, end);

        BigDecimal totalGrams = list.stream()
                .map(Production::getQuantityGrams)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pureGold = list.stream()
                .map(p -> {
                    BigDecimal grams = p.getQuantityGrams() != null ? p.getQuantityGrams() : BigDecimal.ZERO;
                    BigDecimal purity = p.getGradePurity() != null ? p.getGradePurity() : BigDecimal.valueOf(100);
                    return grams.multiply(purity).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> result = new HashMap<>();
        result.put("grams", totalGrams.setScale(1, RoundingMode.HALF_UP));
        result.put("pureGold", pureGold.setScale(2, RoundingMode.HALF_UP));
        return result;
    }

    // Employee & Payroll Stats
    public Map<String, Long> getEmployeeAndPayrollStats() {
        long activeEmployees = employeeRepository.countByTerminationDateIsNull();
        long totalEmployees = employeeRepository.count();

        BigDecimal monthlyPayroll = employeeRepository.findAllByTerminationDateIsNull().stream()
                .map(emp -> emp.getBaseSalary()
                        .add(emp.getHousingAllowance())
                        .add(emp.getTransportAllowance())
                        .add(emp.getOtherFixedAllowance()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Long> stats = new HashMap<>();
        stats.put("activeEmployees", activeEmployees);
        stats.put("totalEmployees", totalEmployees);
        stats.put("monthlyPayroll", monthlyPayroll.longValue());
        return stats;
    }

    // Recent Activity (last 6 production entries with operator name)
    public List<Map<String, Object>> getRecentActivity(int limit) {
        return productionRepository.findTop6ByOrderByCreatedAtDesc().stream()
                .map(p -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("id", p.getId());
                    item.put("name", p.getOperator() != null ? p.getOperator().getFullName() : "Unknown");
                    item.put("action", "Recorded " + p.getQuantityGrams().stripTrailingZeros().toPlainString() + "g");
                    item.put("timestamp", p.getCreatedAt() != null ? p.getCreatedAt() : OffsetDateTime.now());
                    return item;
                })
                .collect(Collectors.toList());
    }

    // Last 7 Days Pure Gold Trend (for chart)
    public List<Map<String, Object>> getLast7DaysTrend() {
        LocalDate end = LocalDate.now().plusDays(1);
        LocalDate start = end.minusDays(7);

        OffsetDateTime startODT = start.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime endODT = end.atStartOfDay().atOffset(ZoneOffset.UTC);

        List<Production> productions = productionRepository.findByCreatedAtBetween(startODT, endODT);

        // Group by date
        Map<LocalDate, BigDecimal> dailyPure = productions.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCreatedAt().toLocalDate(),
                        Collectors.reducing(BigDecimal.ZERO,
                                p -> {
                                    BigDecimal g = p.getQuantityGrams() != null ? p.getQuantityGrams() : BigDecimal.ZERO;
                                    BigDecimal pty = p.getGradePurity() != null ? p.getGradePurity() : BigDecimal.valueOf(100);
                                    return g.multiply(pty).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                                },
                                BigDecimal::add)
                ));

        // Build ordered list for last 7 days (including empty days)
        List<Map<String, Object>> trend = new ArrayList<>();
        for (LocalDate date = start.minusDays(1); date.isBefore(end); date = date.plusDays(1)) {
            BigDecimal pure = dailyPure.getOrDefault(date, BigDecimal.ZERO);
            Map<String, Object> point = new HashMap<>();
            point.put("date", date.format(java.time.format.DateTimeFormatter.ofPattern("dd MMM")));
            point.put("pureGold", pure.setScale(1, RoundingMode.HALF_UP));
            trend.add(point);
        }
        return trend;
    }
}