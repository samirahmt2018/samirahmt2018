package com.gundan.terragold.mapper;

import com.gundan.terragold.dto.EmployeeDto;
import com.gundan.terragold.dto.request.EmployeeCreateRequest;
import com.gundan.terragold.entity.Employee;

public class EmployeeMapper {

    public static EmployeeDto toDto(Employee e) {
        return new EmployeeDto(
                e.getId(),
                e.getEmployeeCode(),
                e.getFullName(),
                e.getPhoneNumber(),
                e.getRole(),
                e.getDepartment(),
                e.getHireDate(),
                e.getTerminationDate(),
                e.getMonthlySalary(),
                e.getNotes(), // MAPPED THE NEW FIELD
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    public static Employee fromCreateRequest(EmployeeCreateRequest req) {
        return Employee.builder()
                .employeeCode(req.employeeCode())
                .fullName(req.fullName())
                .phoneNumber(req.phoneNumber())
                .role(req.role())
                .department(req.department())
                .hireDate(req.hireDate())
                .monthlySalary(req.monthlySalary())
                .notes(req.notes())
                .build();
    }
}