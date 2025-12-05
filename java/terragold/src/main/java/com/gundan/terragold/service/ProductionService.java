package com.gundan.terragold.service;

import com.gundan.terragold.dto.*;
import com.gundan.terragold.dto.request.ProductionCreateRequest;
import com.gundan.terragold.dto.request.ProductionUpdateRequest;
import com.gundan.terragold.entity.*;
import com.gundan.terragold.mapper.ProductionMapper;
import com.gundan.terragold.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductionService {

    private final ProductionRepository productionRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public ProductionDto createProduction(ProductionCreateRequest request) {
        Employee operator = employeeRepository.findById(request.getOperatorId())
                .orElseThrow(() -> new RuntimeException("Operator not found"));

        Production production = Production.builder()
                .productionDate(request.getProductionDate())
                .quantityGrams(request.getQuantityGrams())
                .gradePurity(request.getGradePurity())
                .sourceBatchId(request.getSourceBatchId())
                .operator(operator)
                .build();

        Production saved = productionRepository.save(production);
        return ProductionMapper.toDto(saved);
    }

    @Transactional
    public ProductionDto updateProduction(Long id, ProductionUpdateRequest request) {
        Production production = productionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Production record not found"));

        if (request.getOperatorId() != null) {
            Employee operator = employeeRepository.findById(request.getOperatorId())
                    .orElseThrow(() -> new RuntimeException("Operator not found"));
            production.setOperator(operator);
        }

        if (request.getProductionDate() != null) production.setProductionDate(request.getProductionDate());
        if (request.getQuantityGrams() != null) production.setQuantityGrams(request.getQuantityGrams());
        if (request.getGradePurity() != null) production.setGradePurity(request.getGradePurity());
        if (request.getSourceBatchId() != null) production.setSourceBatchId(request.getSourceBatchId());

        Production updated = productionRepository.save(production);
        return ProductionMapper.toDto(updated);
    }

    @Transactional(readOnly = true)
    public List<ProductionDto> listAllProductions() {
        return productionRepository.findAll().stream()
                .map(ProductionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductionDto getProduction(Long id) {
        Production production = productionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Production record not found"));
        return ProductionMapper.toDto(production);
    }

    @Transactional
    public void deleteProduction(Long id) {
        productionRepository.deleteById(id);
    }

}
