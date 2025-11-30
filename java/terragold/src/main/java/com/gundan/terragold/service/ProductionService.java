package com.gundan.terragold.service;

import com.gundan.terragold.entity.Employee;
import com.gundan.terragold.entity.Production;
import com.gundan.terragold.repository.ProductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductionService {

    private final ProductionRepository productionRepo;

    /**
     * Record a daily production entry using individual parameters.
     */
    @Transactional
    public Production logDaily(BigDecimal grams, BigDecimal purity, Long operatorId, String batchId) {
        Employee operator = Employee.builder().id(operatorId).build();
        Production production = Production.builder()
                .productionDate(LocalDate.now())
                .quantityGrams(grams)
                .gradePurity(purity)
                .operator(operator)
                .sourceBatchId(batchId)
                .build();
        return productionRepo.save(production);
    }

    /**
     * Record a daily production entry using full Production entity (from controller).
     */
    @Transactional
    public Production logDaily(Production production) {
        if (production.getProductionDate() == null) {
            production.setProductionDate(LocalDate.now());
        }
        return productionRepo.save(production);
    }

    /**
     * Get all production records.
     */
    public List<Production> getAllProduction() {
        return productionRepo.findAll();
    }

    /**
     * Get total gold produced this month.
     */
    public BigDecimal getTotalGoldThisMonth() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now();
        BigDecimal total = productionRepo.getTotalGoldProducedBetween(start, end);
        return total != null ? total : BigDecimal.ZERO;
    }
}
