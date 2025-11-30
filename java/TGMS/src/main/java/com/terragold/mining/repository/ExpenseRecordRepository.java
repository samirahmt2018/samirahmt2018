package com.terragold.mining.repository;

import com.terragold.mining.entity.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRecordRepository extends JpaRepository<ExpenseRecordRepository, Long> {
    List<Production> findByDateBetween(LocalDate start, LocalDate end);

}