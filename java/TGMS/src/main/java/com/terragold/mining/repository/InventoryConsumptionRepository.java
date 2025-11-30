package com.terragold.mining.repository;

import com.terragold.mining.entity.InventoryConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InventoryConsumptionRepository extends JpaRepository<InventoryConsumption, Long> {
    List<InventoryConsumption> findByDateBetween(LocalDate start, LocalDate end);
}