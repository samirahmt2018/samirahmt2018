package com.gundan.terragold.repository;

import com.gundan.terragold.entity.InventoryConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface InventoryConsumptionRepository extends JpaRepository<InventoryConsumption, Long> {
    List<InventoryConsumption> findByDateOfIssueBetween(LocalDate from, LocalDate to);
    List<InventoryConsumption> findByItemTypeId(Long itemTypeId);
    List<InventoryConsumption> findByMachineId(Long machineId);
}