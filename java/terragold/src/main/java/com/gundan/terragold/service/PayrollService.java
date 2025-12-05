package com.gundan.terragold.service;

import com.gundan.terragold.entity.*;
import com.gundan.terragold.enums.PayrollItemStatus;
import com.gundan.terragold.enums.PayrollStatus;
import com.gundan.terragold.exception.ConflictException;
import com.gundan.terragold.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final EmployeeRepository employeeRepository;
    private final PayrollRunRepository payrollRunRepository;
    private final PayrollInputRepository payrollInputRepository;
    private final PayrollItemRepository payrollItemRepository;
    /** ---------------------------------------------------------
     * List payroll RUN
     * --------------------------------------------------------- */
    @Transactional
    public List<PayrollRun> listPayrollRuns() {
        return payrollRunRepository.findAll();
    }

    /** ---------------------------------------------------------
     * GENERATE PAYROLL RUN
     * --------------------------------------------------------- */
    @Transactional
    public PayrollRun generatePayroll(String payrollMonth, boolean applyTax, boolean applyPension) {

        YearMonth ym = YearMonth.parse(payrollMonth);
        int year = ym.getYear();
        int month = ym.getMonthValue();

        // prevent duplicates
        payrollRunRepository.findByPeriodYearAndPeriodMonth(year, month)
                .ifPresent(run -> {
                    throw new ConflictException("Payroll run already exists for " + payrollMonth);
                });

        List<Employee> employees = employeeRepository.findActiveEmployees();

        PayrollRun run = PayrollRun.builder()
                .periodYear(year)
                .periodMonth(month)
                .payrollMonth(payrollMonth)
                .applyTax(applyTax)
                .applyPension(applyPension)
                .status(PayrollStatus.GENERATED)
                .generatedAt(OffsetDateTime.now())
                .build();

        // Build payroll items for each employee
        List<PayrollItem> items = employees.stream()
                .map(emp -> buildPayrollItem(run, emp, year, month))
                .toList();

        run.setItems(items);

        // Cascade will save items
        return payrollRunRepository.save(run);
    }

    /** ---------------------------------------------------------
     * BUILD PAYROLL ITEM
     * --------------------------------------------------------- */
    private PayrollItem buildPayrollItem(PayrollRun run, Employee emp, int year, int month) {

        YearMonth ym = YearMonth.of(year, month);

        PayrollInput input = payrollInputRepository
                .findByEmployeeIdAndPeriod(emp.getId(), ym)
                .orElse(null);

        // Base values from employee
        BigDecimal base = emp.getBaseSalary();
        BigDecimal housing = emp.getHousingAllowance();
        BigDecimal transport = emp.getTransportAllowance();
        BigDecimal otherAllow = emp.getOtherFixedAllowance();

        // Optional overrides from payroll input
        BigDecimal overtimeHours = (input != null) ? input.getOvertimeHours() : BigDecimal.ZERO;
        BigDecimal overtimeRate  = (input != null) ? input.getOvertimeRate()  : BigDecimal.ZERO;
        BigDecimal bonus         = (input != null) ? input.getBonusAmount()         : BigDecimal.ZERO;
        BigDecimal deduction     = (input != null) ? input.getDeduction()     : BigDecimal.ZERO;

        BigDecimal overtimeAmount = overtimeHours.multiply(overtimeRate);

        BigDecimal gross = base
                .add(housing)
                .add(transport)
                .add(otherAllow)
                .add(overtimeAmount)
                .add(bonus);
        BigDecimal pensionEmployee = BigDecimal.ZERO;
        BigDecimal pensionEmployer = BigDecimal.ZERO;
        if(run.isApplyPension()) {
            pensionEmployee = calculateEmployeePension(gross);
            pensionEmployer = calculateEmployerPension(gross);
        }
        BigDecimal taxableIncome = gross.subtract(pensionEmployee);
        BigDecimal tax = BigDecimal.ZERO;
        if(run.isApplyTax()) {
            tax = calculateTax(taxableIncome);
        }

        BigDecimal netPay = gross
                .subtract(tax)
                .subtract(pensionEmployee)
                .subtract(deduction);

        return PayrollItem.builder()
                .payrollRun(run)
                .employee(emp)
                .baseSalary(base)
                .housingAllowance(housing)
                .transportAllowance(transport)
                .otherFixedAllowance(otherAllow)
                .overtimeAmount(overtimeAmount)
                .bonusAmount(bonus)
                .taxAmount(tax)
                .pensionEmployee(pensionEmployee)
                .pensionEmployer(pensionEmployer)
                .deduction(deduction)
                .netPay(netPay)
                .build();
    }

    /** Utility to prevent null BigDecimals */
    private BigDecimal n(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    /** ---------------------------------------------------------
     * TAX & PENSION PLACEHOLDERS
     * --------------------------------------------------------- */
    public BigDecimal calculateTax(BigDecimal gross) {
        if (gross == null || gross.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        double g = gross.doubleValue();
        double tax;

        if (g <= 2000) {
            tax = 0;
        } else if (g <= 3200) {
            tax = g * 0.15 - 300;
        } else if (g <= 5250) {
            tax = g * 0.20 - 460;
        } else if (g <= 7800) {
            tax = g * 0.25 - 780;
        } else if (g <= 10900) {
            tax = g * 0.30 - 1170;
        } else {
            tax = g * 0.35 - 1715;
        }

        // Ensure non-negative tax
        if (tax < 0) tax = 0;

        return BigDecimal.valueOf(tax).setScale(2, RoundingMode.HALF_UP);
    }


    public BigDecimal calculateEmployeePension(BigDecimal basicSalary) {
        return basicSalary.multiply(BigDecimal.valueOf(0.07))
                .setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal calculateEmployerPension(BigDecimal basicSalary) {
        return basicSalary.multiply(BigDecimal.valueOf(0.11))
                .setScale(2, RoundingMode.HALF_UP);
    }
    /** ---------------------------------------------------------
     * APPROVE PAYROLL
     * --------------------------------------------------------- */
    @Transactional
    public PayrollRun approvePayroll(String payrollMonth, Long approverId) {

        PayrollRun run = payrollRunRepository.findByPayrollMonth(payrollMonth)
                .orElseThrow(() -> new RuntimeException("Payroll not found for " + payrollMonth));

        run.setStatus(PayrollStatus.APPROVED);
        run.setApprovedAt(OffsetDateTime.now());
        run.setApprovedBy(approverId);

        run.getItems().forEach(i -> i.setStatus(PayrollItemStatus.APPROVED));

        return payrollRunRepository.save(run);
    }

    /** ---------------------------------------------------------
     * PAY PAYROLL
     * --------------------------------------------------------- */
    @Transactional
    public PayrollRun payPayroll(String payrollMonth) {

        PayrollRun run = payrollRunRepository.findByPayrollMonth(payrollMonth)
                .orElseThrow(() -> new RuntimeException("Payroll not found for " + payrollMonth));

        run.setStatus(PayrollStatus.PAID);
        run.setPaidAt(OffsetDateTime.now());

        run.getItems().forEach(i -> i.setStatus(PayrollItemStatus.PAID));

        return payrollRunRepository.save(run);
    }
}
