package com.terragold.mining.service;

import com.terragold.mining.entity.Production;
import com.terragold.mining.repository.ProductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
@Service
@RequiredArgsConstructor
public class ProductionService {
    private final ProductionRepository repository;

    public Production logDaily(Production prod) {
        return repository.save(prod);
    }

    public List<Production> getWeeklySummary(LocalDate start, LocalDate end) {
        return repository.findByDateBetween(start, end);
    }

    // P-2: Monthly aggregate
    public BigDecimal getMonthlyTotal(int year, int month) {
        return repository.getMonthlyTotal(year, month);
    }
}