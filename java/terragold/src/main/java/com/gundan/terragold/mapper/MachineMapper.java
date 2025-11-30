package com.gundan.terragold.mapper;

import com.gundan.terragold.dto.MachineDto;
import com.gundan.terragold.dto.request.MachineCreateRequest;
import com.gundan.terragold.entity.Machine;

public class MachineMapper {

    public static MachineDto toDto(Machine m) {
        return new MachineDto(
                m.getId(),
                m.getMachineType(),
                m.getAssetMachineId(),
                m.getDescription(),
                m.getCreatedAt(),
                m.getUpdatedAt()
        );
    }

    public static Machine fromCreateRequest(MachineCreateRequest req) {
        return Machine.builder()
                .machineType(req.machineType())
                .assetMachineId(req.assetMachineId())
                .description(req.Description())
                .build();
    }
}