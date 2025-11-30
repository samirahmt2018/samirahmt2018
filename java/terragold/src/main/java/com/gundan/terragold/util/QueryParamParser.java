package com.gundan.terragold.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gundan.terragold.dto.request.base.FilterField;
import com.gundan.terragold.dto.request.base.OrFilterGroup;

import java.util.List;

public class QueryParamParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<FilterField> parseFilters(String json) {
        if (json == null || json.isEmpty()) return null;
        try {
            return objectMapper.readValue(json, new TypeReference<List<FilterField>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Invalid filters JSON: " + json, e);
        }
    }

    public static List<OrFilterGroup> parseOrGroups(String json) {
        if (json == null || json.isEmpty()) return null;
        try {
            return objectMapper.readValue(json, new TypeReference<List<OrFilterGroup>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Invalid OR filters JSON: " + json, e);
        }
    }
}

