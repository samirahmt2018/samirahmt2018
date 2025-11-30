package com.gundan.terragold.repository;

import com.gundan.terragold.entity.Employee;
import com.gundan.terragold.enums.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends
        JpaRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee> {

    @Query("SELECT e FROM Employee e WHERE e.terminationDate IS NULL")
    List<Employee> findActiveEmployees();

    List<Employee> findByRole(EmployeeRole role);
}
