package com.gundan.terragold.dto.request.base;

public record PaginationField(
        Integer page,
        Integer pageSize
) {
    public PaginationField {
        if (page == null) page = 1;
        if (pageSize == null) pageSize = 10;
    }
}