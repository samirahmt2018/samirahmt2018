package com.gundan.terragold.dto.request.base;

import com.gundan.terragold.enums.SortingDirection;

public record SortField(
        String field,
        SortingDirection direction // ASC / DESC
) {}
