package com.gundan.terragold.controller;

import com.gundan.terragold.dto.request.GeneratePayrollRequest;
import com.gundan.terragold.dto.request.base.*;
import com.gundan.terragold.entity.PayrollRun;
import com.gundan.terragold.enums.SortingDirection;
import com.gundan.terragold.mapper.MachineMapper;
import com.gundan.terragold.mapper.PayrollRunMapper;
import com.gundan.terragold.repository.PayrollRunRepository;
import com.gundan.terragold.service.GenericListService;
import com.gundan.terragold.service.PayrollService;
import com.gundan.terragold.util.ApiResponseBuilder;
import com.gundan.terragold.util.QueryParamParser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;
    private final GenericListService genericListService;
    private final ApiResponseBuilder api;
    private final PayrollRunRepository payrollRunRepository;
    /* ------------------------------------------------------
      LIST Machines (Generic)
      Accepts: pagination, filters, OR filters, sorting
   ------------------------------------------------------- */
    @GetMapping
    public Object getRuns(
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

        return genericListService.getList(payrollRunRepository, req, PayrollRunMapper::toDto);
    }

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public PayrollRun generate(@Valid @RequestBody GeneratePayrollRequest req) {
        boolean applyTax = req.isApplyTax();
        boolean applyPension = req.isApplyPension();

        return payrollService.generatePayroll(req.getPayrollMonth(), applyTax, applyPension);
    }

    @PostMapping("/approve/{month}")
    public PayrollRun approve(@PathVariable("month") String month, @RequestParam Long approverId) {
        return payrollService.approvePayroll(month, approverId);
    }

    @PostMapping("/pay/{month}")
    public PayrollRun pay(@PathVariable("month") String month) {
        return payrollService.payPayroll(month);
    }
}