package com.gundan.terragold.controller;

import com.gundan.terragold.dto.ItemTypeDto;
import com.gundan.terragold.dto.request.ItemTypeCreateRequest;
import com.gundan.terragold.dto.request.base.*;
import com.gundan.terragold.enums.SortingDirection;
import com.gundan.terragold.mapper.ItemTypeMapper;
import com.gundan.terragold.repository.ItemTypeRepository;
import com.gundan.terragold.service.GenericListService;
import com.gundan.terragold.service.ItemTypeService;
import com.gundan.terragold.util.ApiResponseBuilder;
import com.gundan.terragold.util.QueryParamParser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itemTypes")
@RequiredArgsConstructor
public class ItemTypeController {

    private final ItemTypeService itemTypeService;
    private final GenericListService genericListService;
    private final ApiResponseBuilder api;
    private final ItemTypeRepository itemTypeRepository;
    /* ------------------------------------------------------
      LIST ItemTypes (Generic)
      Accepts: pagination, filters, OR filters, sorting
   ------------------------------------------------------- */
    @GetMapping
    public Object getItemTypes(
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

        return genericListService.getList(itemTypeRepository, req, ItemTypeMapper::toDto);
    }
    /* ------------------------------------------------------
       CREATE ENDPOINT: POST /api/v1/itemTypes
    ------------------------------------------------------- */
    @PostMapping
    public Object createItemType(@Valid @RequestBody(required = true) ItemTypeCreateRequest request) {
        ItemTypeDto created = itemTypeService.create(request);

       return api.buildResponse(
               created,
               null,
                null,
                null,
               null,
              List.of("ItemType created) successfully"),
               HttpStatus.CREATED
       );
    }

    /* ------------------------------------------------------
       SHOW ENDPOINT: GET /api/v1/itemTypes/{id}
    ------------------------------------------------------- */
    @GetMapping("/{id}")
    public Object getItemTypeById(@PathVariable Long id) {
        ItemTypeDto itemTypeDto = itemTypeService.findById(id);

        return api.buildResponse(
                itemTypeDto,
                null,
                null,
                null,
                null,
                null,
                HttpStatus.OK
        );
    }
}