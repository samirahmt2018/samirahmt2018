package com.terragold.mining.repository;

import com.terragold.mining.entity.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ProductionRepository extends JpaRepository<Production, Long> {
    List<Production> findByDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT SUM(p.quantity) FROM Production p WHERE YEAR(p.date) = :year AND MONTH(p.date) = :month")
    BigDecimal getMonthlyTotal(@Param("year") int year, @Param("month") int month);
}