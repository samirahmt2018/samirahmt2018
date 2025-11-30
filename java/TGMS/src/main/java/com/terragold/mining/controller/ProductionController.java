package com.terragold.mining.controller;

import com.terragold.mining.entity.Production;
import com.terragold.mining.service.ProductionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductionController {
    private final ProductionService service;

    @PostMapping("/log") // P-1
    @PreAuthorize("hasRole('FOREMAN') or hasRole('ADMIN')")
    public Production log(@Valid @RequestBody Production prod) {
        return service.logDaily(prod);
    }

    @GetMapping("/summary/weekly") // P-2
    @PreAuthorize("hasRole('ADMIN')")
    public List<Production> weekly(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        return service.getWeeklySummary(start, end);
    }
}