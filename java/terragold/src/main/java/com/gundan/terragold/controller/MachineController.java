package com.gundan.terragold.controller;

import com.gundan.terragold.dto.MachineDto;
import com.gundan.terragold.dto.request.MachineCreateRequest;
import com.gundan.terragold.dto.request.base.*;
import com.gundan.terragold.mapper.EmployeeMapper;
import com.gundan.terragold.mapper.MachineMapper;
import com.gundan.terragold.repository.MachineRepository;
import com.gundan.terragold.service.GenericListService;
import com.gundan.terragold.service.MachineService;
import com.gundan.terragold.util.ApiResponseBuilder;
import com.gundan.terragold.util.QueryParamParser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/machines")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;
    private final GenericListService genericListService;
    private final ApiResponseBuilder api;
    private final MachineRepository machineRepository;
    /* ------------------------------------------------------
      LIST Machines (Generic)
      Accepts: pagination, filters, OR filters, sorting
   ------------------------------------------------------- */
    @GetMapping
    public Object getMachines(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "ASC") String sortDir,
            @RequestParam(required = false) String filters,
            @RequestParam(required = false) String orFilters
    ) {

        // Parse query strings safely
        List<FilterField> parsedFilters = filters != null
                ? QueryParamParser.parseFilters(filters)
                : List.of();

        List<OrFilterGroup> parsedOrFilters = orFilters != null
                ? QueryParamParser.parseOrGroups(orFilters)
                : List.of();

        ListQueryRequest req = new ListQueryRequest(
                new PaginationField(page, pageSize),
                parsedFilters,
                parsedOrFilters,
                new SortField(sortField, sortDir)
        );

        return genericListService.getList(machineRepository, req, MachineMapper::toDto);
    }
    /* ------------------------------------------------------
       CREATE ENDPOINT: POST /api/v1/machines
    ------------------------------------------------------- */
    @PostMapping
    public Object createMachine(@Valid @RequestBody(required = true) MachineCreateRequest request) {
        MachineDto created = machineService.create(request);

       return api.buildResponse(
               created,
               null,
                null,
                null,
               null,
              List.of("Machine created) successfully"),
               HttpStatus.CREATED
       );
    }

    /* ------------------------------------------------------
       SHOW ENDPOINT: GET /api/v1/machines/{id}
    ------------------------------------------------------- */
    @GetMapping("/{id}")
    public Object getMachineById(@PathVariable Long id) {
        MachineDto machineDto = machineService.findById(id);

        return api.buildResponse(
                machineDto,
                null,
                null,
                null,
                null,
                null,
                HttpStatus.OK
        );
    }
}