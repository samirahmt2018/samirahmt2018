package com.gundan.terragold.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gundan.terragold.dto.request.base.FilterField;
import com.gundan.terragold.dto.request.base.ListQueryRequest;
import com.gundan.terragold.dto.request.base.OrFilterGroup;
import com.gundan.terragold.dto.request.base.PaginationField;
import com.gundan.terragold.dto.request.base.SortField;
import com.gundan.terragold.enums.SortingDirection;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QueryParamParser {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // ------------------------- Main Entry -------------------------
    /**
     * Parses the full query parameter map and builds a ListQueryRequest.
     */
    public static ListQueryRequest parse(Map<String, String> queryParams) {

        // 1️⃣ Pagination
        PaginationField pagination = parsePagination(queryParams, 1, 10);

        // 2️⃣ Sorting
        SortField sort = parseSorting(queryParams, "createdAt", SortingDirection.ASC);

        // 3️⃣ Filters (AND)
        List<FilterField> filters = parseFilters(queryParams.get("filters"));

        // 4️⃣ OR Filters
        List<OrFilterGroup> orFilters = parseOrGroups(queryParams.get("orFilters"));

        // 5️⃣ Build unified request
        return new ListQueryRequest(pagination, filters, orFilters, sort);
    }

    // ------------------------- Pagination -------------------------
    public static PaginationField parsePagination(Map<String, String> queryParams,
                                                  int defaultPage,
                                                  int defaultPageSize) {
        int page = defaultPage;
        int pageSize = defaultPageSize;

        try {
            if (queryParams.containsKey("page")) page = Integer.parseInt(queryParams.get("page"));
            else if (queryParams.containsKey("pagination.page")) page = Integer.parseInt(queryParams.get("pagination.page"));

            if (queryParams.containsKey("pageSize")) pageSize = Integer.parseInt(queryParams.get("pageSize"));
            else if (queryParams.containsKey("pagination.pageSize")) pageSize = Integer.parseInt(queryParams.get("pagination.pageSize"));
        } catch (NumberFormatException ignored) {
            // fallback to defaults
        }

        if (page < 1) page = defaultPage;
        if (pageSize < 1) pageSize = defaultPageSize;

        return new PaginationField(page, pageSize);
    }

    // ------------------------- Sorting -------------------------
    public static SortField parseSorting(Map<String, String> queryParams,
                                         String defaultSortField,
                                         SortingDirection defaultSortDir) {

        String sortField = queryParams.getOrDefault("sortField",
                queryParams.getOrDefault("sort.field", defaultSortField));

        String dirValue = queryParams.getOrDefault("sortDir",
                queryParams.getOrDefault("sort.dir", defaultSortDir.name()));

        SortingDirection sortDir;
        try {
            sortDir = SortingDirection.valueOf(dirValue.toUpperCase());
        } catch (Exception e) {
            sortDir = defaultSortDir;
        }

        return new SortField(sortField, sortDir);
    }

    // ------------------------- Filters -------------------------
    public static List<FilterField> parseFilters(String json) {
        return parseJsonList(json, new TypeReference<List<FilterField>>() {}, "filters");
    }

    public static List<OrFilterGroup> parseOrGroups(String json) {
        return parseJsonList(json, new TypeReference<List<OrFilterGroup>>() {}, "orGroups");
    }

    private static <T> List<T> parseJsonList(String json, TypeReference<List<T>> typeRef, String fieldName) {
        if (json == null || json.trim().isEmpty()) return Collections.emptyList();

        try {
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JSON for " + fieldName + ": " + json, e);
        }
    }
}
