package com.gundan.terragold.mapper;

import com.gundan.terragold.dto.ProductionDto;
import com.gundan.terragold.dto.request.ProductionCreateRequest;
import com.gundan.terragold.entity.Employee;
import com.gundan.terragold.entity.Production;
import com.gundan.terragold.repository.EmployeeRepository;

public class ProductionMapper {

    public static ProductionDto toDto(Production m) {
        return new ProductionDto(
                m.getId(),
                m.getProductionDate(),
                m.getQuantityGrams(),
                m.getOperator().getFullName(),
                m.getGradePurity(),
                m.getSourceBatchId(),
                m.getCreatedAt(),
                m.getUpdatedAt()

        );
    }

    public static Production fromCreateRequest(ProductionCreateRequest req) {
        Employee operator = Employee.builder().id(req.getOperatorId()).build();
        return Production.builder()
                .productionDate(req.getProductionDate())
                .quantityGrams(req.getQuantityGrams())
                .gradePurity(req.getGradePurity())
                .sourceBatchId(req.getSourceBatchId())
                .operator(operator)
                .build();
    }
}