package com.gundan.terragold.util;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class ApiResponseBuilder {

    public <T> ResponseEntity<ApiResponse<T>> buildResponse(T payload, Map<String, String> links,
                                                            Map<String, Object> attributes,
                                                            List<String> warnings, List<List<Map<String, String>>> errors,
                                                            List<String> info, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setPayload(payload);
        response.set_links(links != null ? links : new LinkedHashMap<>());
        response.set_attributes(attributes != null ? attributes : new LinkedHashMap<>());
        response.set_warning(warnings != null ? warnings : new ArrayList<>());
        response.set_errors(errors != null ? errors : new ArrayList<>());
        response.set_info(info != null ? info : new ArrayList<>());
        response.set_generated(new ApiResponse.Generated(ZonedDateTime.now()));
        return new ResponseEntity<>(response, status);
    }

    public <T> ResponseEntity<ApiResponse<T>> buildErrorResponse(String errorTitle, String errorMessage,
                                                                 HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>();
        List<Map<String, String>> errorDetails = new ArrayList<>();
        errorDetails.add(Map.of("title", errorTitle, "message", errorMessage));
        response.set_errors(List.of(errorDetails));
        return new ResponseEntity<>(response, status);
    }

    public <T> ResponseEntity<ApiResponse<T>> buildPaginatedResponse(T payload, int page, int pageSize, long total,
                                                                     HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setPayload(payload);

        // Pagination links
        Map<String, String> links = new LinkedHashMap<>();
        int lastPage = (int) Math.ceil((double) total / pageSize);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentRequest().build().toString();
        if (page > 1) {
            links.put("previousPage", String.format("%s?pagination[page]=%d", baseUrl, page - 1));
        } else {
            links.put("previousPage", null);
        }
        if (page < lastPage) {
            links.put("nextPage", String.format("%s?pagination[page]=%d", baseUrl, page + 1));
        } else {
            links.put("nextPage", null);
        }
        response.set_links(links);

        // Pagination attributes
        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("pagination", Map.of(
                "pageSize", pageSize,
                "page", page,
                "total", total,
                "lastPage", lastPage
        ));
        response.set_attributes(attributes);

        // Warnings for default pagination
        List<String> warnings = new ArrayList<>();
        if (page == 1) {
            warnings.add("No page number given. Assuming first page. You can set a page by using the pagination[page] parameter.");
        }
        if (pageSize == 10) {
            warnings.add("No page size given. Assuming default page size of 10. Use the parameter pagination[pageSize] to supply a custom page size.");
        }
        response.set_warning(warnings);

        return new ResponseEntity<>(response, status);
    }
}
