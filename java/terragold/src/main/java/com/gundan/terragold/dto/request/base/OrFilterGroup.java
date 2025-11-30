package com.gundan.terragold.dto.request.base;

import java.util.List;

public record OrFilterGroup(
        List<FilterField> filters
) {}
