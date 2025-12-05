package com.gundan.terragold.service;

import com.gundan.terragold.dto.PayrollInputDto;
import com.gundan.terragold.dto.request.PayrollInputCreateRequest;
import com.gundan.terragold.dto.request.PayrollInputUpdateRequest;
import com.gundan.terragold.entity.Employee;
import com.gundan.terragold.entity.PayrollInput;
import com.gundan.terragold.repository.EmployeeRepository;
import com.gundan.terragold.repository.PayrollInputRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayrollInputService {

    private final PayrollInputRepository payrollInputRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public PayrollInputDto create(PayrollInputCreateRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        payrollInputRepository.findByEmployeeIdAndPeriod(request.getEmployeeId(), request.getPeriod())
                .ifPresent(pi -> {
                    throw new RuntimeException("Payroll input already exists for this employee & period");
                });

        PayrollInput input = new PayrollInput();
        input.setEmployee(employee);
        input.setPeriod(request.getPeriod());
        input.setBonusAmount(request.getBonus());
        input.setDeduction(request.getDeduction());
        input.setOvertimeHours(request.getOvertimeHours());
        input.setOvertimeRate(request.getOvertimeRate());

        payrollInputRepository.save(input);
        return toDto(input);
    }

    @Transactional
    public PayrollInputDto update(Long id, PayrollInputUpdateRequest request) {
        PayrollInput input = payrollInputRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll input not found"));

        input.setBonusAmount(request.getBonus());
        input.setDeduction(request.getDeduction());
        input.setOvertimeHours(request.getOvertimeHours());
        input.setOvertimeRate(request.getOvertimeRate());

        payrollInputRepository.save(input);
        return toDto(input);
    }

    private PayrollInputDto toDto(PayrollInput input) {
       return new PayrollInputDto(
                input.getId(),
                input.getEmployee().getId(),
                input.getPeriod(),
                input.getBonusAmount(),
                input.getDeduction(),
                input.getOvertimeHours(),
                input.getOvertimeRate());
    }
}