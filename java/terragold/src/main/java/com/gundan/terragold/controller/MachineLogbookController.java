package com.gundan.terragold.controller;

import com.gundan.terragold.dto.MachineLogbookDto;
import com.gundan.terragold.dto.request.MachineLogbookCreateRequest;
import com.gundan.terragold.dto.request.MachineLogbookUpdateRequest;
import com.gundan.terragold.dto.request.base.*;
import com.gundan.terragold.enums.SortingDirection;
import com.gundan.terragold.mapper.MachineLogbookMapper;
import com.gundan.terragold.mapper.MachineMapper;
import com.gundan.terragold.repository.MachineLogbookRepository;
import com.gundan.terragold.service.GenericListService;
import com.gundan.terragold.service.MachineLogbookService;
import com.gundan.terragold.util.ApiResponseBuilder;
import com.gundan.terragold.util.QueryParamParser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machineLogbooks")
@RequiredArgsConstructor
public class MachineLogbookController {

    private final MachineLogbookService service;
    private final GenericListService genericListService;
    private final ApiResponseBuilder api;
    private final MachineLogbookRepository machineLogbookRepository;


    @GetMapping
    public Object getMachines(
            @RequestParam(required = false) PaginationField pagination,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "ASC") SortingDirection sortDir,
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
                pagination,
                parsedFilters,
                parsedOrFilters,
                new SortField(sortField, sortDir)
        );

        return genericListService.getList(machineLogbookRepository, req, MachineLogbookMapper::toDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MachineLogbookDto create(@Valid  @RequestBody MachineLogbookCreateRequest request) {
        return service.createLog(request);
    }

    @PutMapping("/{id}")
    public MachineLogbookDto update(@PathVariable Long id,
                                            @RequestBody MachineLogbookUpdateRequest request) {
        return service.updateLog(id, request);
    }

    @GetMapping("/{id}")
    public MachineLogbookDto get(@PathVariable Long id) {
        return service.getLog(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteLog(id);
    }
}
