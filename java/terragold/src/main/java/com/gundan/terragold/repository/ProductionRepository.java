package com.gundan.terragold.repository;

import com.gundan.terragold.entity.Machine;
import com.gundan.terragold.entity.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public interface ProductionRepository extends
        JpaRepository<Production, Long>,
        JpaSpecificationExecutor<Production>
{
    List<Production> findByProductionDate(LocalDate date);
    List<Production> findByOperatorId(Long operatorId);
    List<Production> findByProductionDateBetween(LocalDate start, LocalDate end);
    List<Production> findByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end);
    List<Production> findTop6ByOrderByCreatedAtDesc();

}
