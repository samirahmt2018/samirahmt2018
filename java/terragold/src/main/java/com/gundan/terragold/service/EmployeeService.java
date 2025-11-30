package com.gundan.terragold.service;

import com.gundan.terragold.dto.EmployeeDto;
import com.gundan.terragold.dto.request.EmployeeCreateRequest;
import com.gundan.terragold.entity.Employee;
import com.gundan.terragold.entity.EmployeeAdvanceAccount;
import com.gundan.terragold.enums.EmployeeRole;
import com.gundan.terragold.mapper.EmployeeMapper;
import com.gundan.terragold.repository.EmployeeAdvanceAccountRepository;
import com.gundan.terragold.repository.EmployeeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final EmployeeAdvanceAccountRepository accountRepo;

    /* ------------------------------------------------------
       CREATE EMPLOYEE (auto-advance account if role requires)
    ------------------------------------------------------- */
    public EmployeeDto create(@Valid @RequestBody EmployeeCreateRequest request) {
        Employee entity = EmployeeMapper.fromCreateRequest(request);
        Employee saved = employeeRepo.save(entity);

        if (requiresAdvanceAccount(saved.getRole())) {
            accountRepo.save(EmployeeAdvanceAccount.builder()
                    .employee(saved)
                    .currentBalance(BigDecimal.ZERO)
                    .build()
            );
        }

        return EmployeeMapper.toDto(saved);
    }
    /* ------------------------------------------------------
           RETRIEVE EMPLOYEE BY ID (SHOW ENDPOINT LOGIC)
        ------------------------------------------------------- */
    public EmployeeDto findById(Long id) {
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        // In a real application, use a custom exception like ResourceNotFoundException

        return EmployeeMapper.toDto(employee);
    }
    /* ------------------------------------------------------
       EXPOSE REPOSITORY FOR GENERIC LIST SERVICE
    ------------------------------------------------------- */
    public EmployeeRepository getRepository() {
        return employeeRepo; // implements both JpaRepository & JpaSpecificationExecutor
    }

    private boolean requiresAdvanceAccount(EmployeeRole role) {
      return  role.equals(EmployeeRole.PURCHASER);
    }
}
