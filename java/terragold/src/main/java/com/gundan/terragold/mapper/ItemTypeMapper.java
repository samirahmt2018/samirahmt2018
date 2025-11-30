package com.gundan.terragold.mapper;

import com.gundan.terragold.dto.ItemTypeDto;
import com.gundan.terragold.dto.ItemTypeDto;
import com.gundan.terragold.dto.request.ItemTypeCreateRequest;
import com.gundan.terragold.entity.ItemType;
import com.gundan.terragold.entity.ItemType;

public class ItemTypeMapper {

    public static ItemTypeDto toDto(ItemType m) {
        return new ItemTypeDto(
                m.getId(),
                m.getName(),
                m.getUnitOfMeasurement(),
                m.getDescription(),
                m.getCreatedAt(),
                m.getUpdatedAt()
        );
    }

    public static ItemType fromCreateRequest(ItemTypeCreateRequest req) {
        return ItemType.builder()
                .name(req.name())
                .unitOfMeasurement(req.unitOfMeasurement())
                .description(req.Description())
                .build();
    }
}