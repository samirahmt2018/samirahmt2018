package com.gundan.terragold.mapper;

import com.gundan.terragold.dto.MachineLogbookDto;
import com.gundan.terragold.dto.request.MachineLogbookCreateRequest;
import com.gundan.terragold.entity.Machine;
import com.gundan.terragold.entity.MachineLogbook;

public class MachineLogbookMapper {

    public static MachineLogbookDto toDto(MachineLogbook m) {
        return new MachineLogbookDto(
                m.getId(),
                m.getMachine().getId(),
                m.getMachine().getMachineType(),
                m.getOperator().getFullName(),
                m.getLogDate(),
                m.getStartTime(),
                m.getStartHourMeter(),
                m.getEndTime(),
                m.getEndHourMeter(),
                m.getTotalHours(),
                m.getRepetitions()
        );
    }

    public static MachineLogbook fromCreateRequest(MachineLogbookCreateRequest req) {
        Machine machine = Machine.builder().id(req.machineId()).build();
        return MachineLogbook.builder()
                .machine(machine)
                .logDate(req.logDate())
                .startTime(req.startTime())
                .startHourMeter(req.startHourMeter())
                .endTime(req.endTime())
                .endHourMeter(req.endHourMeter())
                .repetitions(req.repetitions())
                .build();
    }
}