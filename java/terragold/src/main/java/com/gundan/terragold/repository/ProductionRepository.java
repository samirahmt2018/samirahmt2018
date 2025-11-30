package com.gundan.terragold.repository;

import com.gundan.terragold.entity.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ProductionRepository extends JpaRepository<Production, Long> {
    List<Production> findByProductionDateBetween(LocalDate from, LocalDate to);

    @Query("SELECT COALESCE(SUM(p.quantityGrams), 0) FROM Production p " +
            "WHERE p.productionDate BETWEEN :from AND :to")
    BigDecimal getTotalGoldProducedBetween(LocalDate from, LocalDate to);
}