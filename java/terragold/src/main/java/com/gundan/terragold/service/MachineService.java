package com.gundan.terragold.service;

import com.gundan.terragold.dto.MachineDto;
import com.gundan.terragold.dto.request.MachineCreateRequest;
import com.gundan.terragold.entity.Machine;
import com.gundan.terragold.mapper.MachineMapper;
import com.gundan.terragold.repository.MachineRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Transactional
public class MachineService {

    private final MachineRepository machineRepo;

    /* ------------------------------------------------------
       CREATE MACHINE
    ------------------------------------------------------- */
    public MachineDto create(@Valid @RequestBody MachineCreateRequest request) {
        Machine entity = MachineMapper.fromCreateRequest(request);
        Machine saved = machineRepo.save(entity);

        return MachineMapper.toDto(saved);
    }

    /* ------------------------------------------------------
       RETRIEVE MACHINE BY ID (SHOW ENDPOINT LOGIC)
    ------------------------------------------------------- */
    public MachineDto findById(Long id) {
        Machine machine = machineRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Machine not found with ID: " + id));

        return MachineMapper.toDto(machine);
    }

    /* ------------------------------------------------------
       EXPOSE REPOSITORY FOR GENERIC LIST SERVICE
    ------------------------------------------------------- */
    public MachineRepository getRepository() {
        return machineRepo;
    }
}