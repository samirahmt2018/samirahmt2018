package com.gundan.terragold.dto.request.base;

public record FilterField(
        String field,
        String operation, // =, !=, >, <, >=, <=, like
        String value
) {}
