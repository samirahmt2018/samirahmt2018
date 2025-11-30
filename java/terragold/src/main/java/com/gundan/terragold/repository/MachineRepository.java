package com.gundan.terragold.repository;

import com.gundan.terragold.entity.Employee;
import com.gundan.terragold.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MachineRepository extends
        JpaRepository<Machine, Long>,
        JpaSpecificationExecutor<Machine>
{
}