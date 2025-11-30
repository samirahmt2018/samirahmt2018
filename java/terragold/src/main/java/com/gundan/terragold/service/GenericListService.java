package com.gundan.terragold.service;

import com.gundan.terragold.dto.request.base.FilterField;
import com.gundan.terragold.dto.request.base.ListQueryRequest;
import com.gundan.terragold.dto.request.base.OrFilterGroup;
import com.gundan.terragold.dto.request.base.SortField;
import com.gundan.terragold.util.ApiResponseBuilder;
import com.gundan.terragold.util.spec.GenericSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenericListService {

    private final ApiResponseBuilder api;

    /**
     * Generic list fetcher for any entity.
     *
     * @param repo   JPA Specification Executor repository of the entity
     * @param req    ListQueryRequest containing filters, pagination, and sort
     * @param mapper Function to map entity to DTO
     * @param <T>    Entity type
     * @param <R>    DTO type
     * @return ResponseEntity with paginated ApiResponse
     */
    public <T, R> Object getList(JpaSpecificationExecutor<T> repo,
                                 ListQueryRequest req,
                                 Function<T, R> mapper) {

        // ---------------- Pagination ----------------
        int page = req.pagination() != null && req.pagination().page() != null ? req.pagination().page() : 1;
        int pageSize = req.pagination() != null && req.pagination().pageSize() != null ? req.pagination().pageSize() : 10;

        // ---------------- Sorting ----------------
        Sort sort = Sort.unsorted();
        SortField sortField = req.sort();
        if (sortField != null && sortField.field() != null && !sortField.field().isEmpty()) {
            Sort.Direction dir = "DESC".equalsIgnoreCase(sortField.direction()) ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(dir, sortField.field());
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        // ---------------- Specification / Filters ----------------
        Specification<T> spec = (root, query, cb) -> null; // empty spec to start

        if (req.filter() != null) {
            for (FilterField f : req.filter()) {
                spec = spec.and(GenericSpecification.build(f));
            }
        }

        if (req.orFilters() != null) {
            for (OrFilterGroup group : req.orFilters()) {
                spec = spec.and(GenericSpecification.buildOrGroup(group));
            }
        }

        // ---------------- Fetch data ----------------
        var result = repo.findAll(spec, pageable);

        List<R> dto = result.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());

        // ---------------- Build response ----------------
        return api.buildPaginatedResponse(
                dto,
                page,
                pageSize,
                result.getTotalElements(),
                HttpStatus.OK
        );
    }
}
