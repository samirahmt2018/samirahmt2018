package com.gundan.terragold.controller;

import com.gundan.terragold.dto.request.ProductionCreateRequest;
import com.gundan.terragold.dto.ProductionDto; // You'll need this DTO
import com.gundan.terragold.dto.request.ProductionUpdateRequest;
import com.gundan.terragold.enums.SortingDirection;
import com.gundan.terragold.mapper.ProductionMapper; // Mapper to convert Entity <-> DTO
import com.gundan.terragold.repository.ProductionRepository; // JpaRepository<ProductionEntity, Long>
import com.gundan.terragold.service.GenericListService;
import com.gundan.terragold.service.ProductionService;
import com.gundan.terragold.util.ApiResponseBuilder;
import com.gundan.terragold.util.QueryParamParser;
import com.gundan.terragold.dto.request.base.FilterField;
import com.gundan.terragold.dto.request.base.ListQueryRequest;
import com.gundan.terragold.dto.request.base.OrFilterGroup;
import com.gundan.terragold.dto.request.base.PaginationField;
import com.gundan.terragold.dto.request.base.SortField;
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
@RequestMapping("/api/productions")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionService productionService;
    private final GenericListService genericListService;
    private final ProductionRepository productionRepository;
    private final ApiResponseBuilder api;

    /* ------------------------------------------------------
       LIST Productions (with pagination, filters, sorting, OR filters)
    ------------------------------------------------------- */
    @GetMapping
    public Object getProductions(
            @RequestParam Map<String, String> queryParams
    ) {
        ListQueryRequest req = QueryParamParser.parse(queryParams);
        //
        return genericListService.getList(productionRepository, req, ProductionMapper::toDto);
    }

    /* ------------------------------------------------------
       CREATE Production Record
       POST /api/productions
    ------------------------------------------------------- */
    @PostMapping
    public ResponseEntity<Object> createProduction(@Valid @RequestBody ProductionCreateRequest request) {
        ProductionDto created = productionService.createProduction(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(api.buildResponse(
                        created,
                        null,
                        null,
                        null,
                        null,
                        List.of("Production record created successfully"),
                        HttpStatus.CREATED
                ));
    }

    /* ------------------------------------------------------
       GET Single Production by ID
       GET /api/productions/{id}
    ------------------------------------------------------- */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductionById(@PathVariable Long id) {
        ProductionDto dto = productionService.getProduction(id);

        return ResponseEntity.ok(api.buildResponse(
                dto,
                null,
                null,
                null,
                null,
                null,
                HttpStatus.OK
        ));
    }

    /* ------------------------------------------------------
       UPDATE Production (Full replace or partial via PUT)
       PUT /api/productions/{id}
    ------------------------------------------------------- */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduction(
            @PathVariable Long id,
            @Valid @RequestBody ProductionUpdateRequest request) {

        ProductionDto updated = productionService.updateProduction(id, request);

        return ResponseEntity.ok(api.buildResponse(
                updated,
                null,
                null,
                null,
                null,
                List.of("Production record updated successfully"),
                HttpStatus.OK
        ));
    }

    /* ------------------------------------------------------
       DELETE Production
       DELETE /api/productions/{id}
    ------------------------------------------------------- */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduction(@PathVariable Long id) {
        productionService.deleteProduction(id);

        return ResponseEntity.ok(api.buildResponse(
                null,
                null,
                null,
                null,
                null,
                List.of("Production record deleted successfully"),
                HttpStatus.OK
        ));
    }
}