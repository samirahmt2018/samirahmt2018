package com.gundan.terragold.dto.request.base;

import java.util.List;

public record ListQueryRequest(
        PaginationField pagination,
        List<FilterField> filter,
        List<OrFilterGroup> orFilters,
        SortField sort
) {}
