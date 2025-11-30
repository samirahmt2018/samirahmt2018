package com.gundan.terragold.repository;

import com.gundan.terragold.entity.GeneralExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface GeneralExpenseRepository extends JpaRepository<GeneralExpense, Long> {
    List<GeneralExpense> findByExpenseDateBetween(LocalDate from, LocalDate to);
    List<GeneralExpense> findByCategoryAndExpenseDateBetween(String category, LocalDate from, LocalDate to);
}