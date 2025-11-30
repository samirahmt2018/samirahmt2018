package com.gundan.terragold.dto.request.base;

public record SortField(
        String field,
        String direction // ASC / DESC
) {}
