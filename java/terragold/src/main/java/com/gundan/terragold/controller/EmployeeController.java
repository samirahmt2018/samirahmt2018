package com.gundan.terragold.controller;

import com.gundan.terragold.dto.EmployeeDto;
import com.gundan.terragold.dto.request.EmployeeCreateRequest;
import com.gundan.terragold.dto.request.base.*;
import com.gundan.terragold.mapper.EmployeeMapper;
import com.gundan.terragold.repository.EmployeeRepository;
import com.gundan.terragold.service.EmployeeService;
import com.gundan.terragold.service.GenericListService;
import com.gundan.terragold.util.ApiResponse;
import com.gundan.terragold.util.ApiResponseBuilder;
import com.gundan.terragold.util.QueryParamParser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final GenericListService genericListService;
    private final ApiResponseBuilder api;
    private final EmployeeRepository employeeRepo;


    /* ------------------------------------------------------
       LIST EMPLOYEES (Generic)
       Accepts: pagination, filters, OR filters, sorting
    ------------------------------------------------------- */
    @GetMapping
    public Object getEmployees(
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

        return genericListService.getList(employeeRepo, req, EmployeeMapper::toDto);
    }

    /* ------------------------------------------------------
       CREATE EMPLOYEE
    ------------------------------------------------------- */
    @PostMapping
    public Object createEmployee(@Valid @RequestBody EmployeeCreateRequest request) {
        EmployeeDto created = employeeService.create(request);

        return api.buildResponse(
                created,
                null,
                null,
                null,
                null,
                List.of("Employee created successfully"),
                HttpStatus.CREATED
        );
    }

    /* ------------------------------------------------------
       SHOW ENDPOINT: GET /api/v1/employees/{id}
    ------------------------------------------------------- */
    @GetMapping("/{id}")
    public Object getEmployeeById(@PathVariable Long id) {
        EmployeeDto employeeDto = employeeService.findById(id);

        return api.buildResponse(
                employeeDto,
                null,
                null,
                null,
                null,
                null,
                HttpStatus.OK
        );
    }
}
