package com.gundan.terragold.util.spec;

import com.gundan.terragold.dto.request.base.FilterField;
import com.gundan.terragold.dto.request.base.OrFilterGroup;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

public class GenericSpecification {

    public static <T> Specification<T> build(FilterField f) {
        return (root, query, cb) -> {
            try {
                String field = f.field().trim();
                String op = f.operation().toLowerCase().trim();
                String rawValue = f.value();

                Path<?> path = root;
                for (String part : field.split("\\.")) {
                    path = path.get(part);
                }

                Object value = convert(rawValue, path);

                return switch (op) {
                    case "=", "eq" -> cb.equal(path, value);
                    case "!=", "<>", "ne" -> cb.notEqual(path, value);
//                    case "like", "ilike", "contains" ->
//                            cb.like(cb.lower(path.as(String.class)), "%" + rawValue.toLowerCase() + "%");
//                    case "notlike" ->
//                            cb.notLike(cb.lower(path.as(String.class)), "%" + rawValue.toLowerCase() + "%");
//                    case "startswith" ->
//                            cb.like(cb.lower(path.as(String.class)), rawValue.toLowerCase() + "%");
//                    case "endswith" ->
//                            cb.like(cb.lower(path.as(String.class)), "%" + rawValue.toLowerCase());

                    case "isnull" -> cb.isNull(path);
                    case "isnotnull" -> cb.isNotNull(path);

                    case ">"  -> greaterThan(cb, path, value);
                    case ">=" -> greaterThanOrEqualTo(cb, path, value);
                    case "<"  -> lessThan(cb, path, value);
                    case "<=" -> lessThanOrEqualTo(cb, path, value);

                    default -> cb.conjunction();
                };
            } catch (Exception e) {
                return cb.conjunction();
            }
        };
    }

    // OR group – fully generic-safe, no StreamSupport, no deprecation
    public static <T> Specification<T> buildOrGroup(OrFilterGroup group) {
        Specification<T> result = null;

        for (FilterField filter : group.filters()) {
            Specification<T> current = build(filter);
            if (current != null) {
                result = result == null ? current : result.or(current);
            }
        }

        return result != null ? result : (root, query, cb) -> cb.conjunction();
    }

    // Optional: AND all filters (useful for normal filter lists)
    public static <T> Specification<T> andAll(Iterable<FilterField> filters) {
        Specification<T> result = null;
        for (FilterField f : filters) {
            Specification<T> spec = build(f);
            if (spec != null) {
                result = result == null ? spec : result.and(spec);
            }
        }
        return result != null ? result : (root, query, cb) -> cb.conjunction();
    }

    // ────── THE FOUR PERFECT COMPARISON HELPERS ──────
    @SuppressWarnings("unchecked")
    private static <Y extends Comparable<? super Y>> Predicate greaterThan(
            CriteriaBuilder cb, Path<?> path, Object value) {
        return cb.greaterThan((Expression<Y>) path, (Y) value);
    }

    @SuppressWarnings("unchecked")
    private static <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(
            CriteriaBuilder cb, Path<?> path, Object value) {
        return cb.greaterThanOrEqualTo((Expression<Y>) path, (Y) value);
    }

    @SuppressWarnings("unchecked")
    private static <Y extends Comparable<? super Y>> Predicate lessThan(
            CriteriaBuilder cb, Path<?> path, Object value) {
        return cb.lessThan((Expression<Y>) path, (Y) value);
    }

    @SuppressWarnings("unchecked")
    private static <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(
            CriteriaBuilder cb, Path<?> path, Object value) {
        return cb.lessThanOrEqualTo((Expression<Y>) path, (Y) value);
    }

    // ────── SMART VALUE CONVERSION ──────
    private static Object convert(String input, Path<?> path) {
        if (input == null || input.isBlank()) return null;

        Class<?> type = path.getJavaType();
        if (type == null) return input.trim();

        String v = input.trim();

        try {
            if (type == String.class) return v;
            if (type == Integer.class || type == int.class) return Integer.parseInt(v);
            if (type == Long.class || type == long.class) return Long.parseLong(v);
            if (type == Double.class || type == double.class) return Double.parseDouble(v);
            if (type == Float.class || type == float.class) return Float.parseFloat(v);
            if (type == Boolean.class || type == boolean.class) return Boolean.parseBoolean(v);
            if (type == LocalDate.class) return LocalDate.parse(v);
            if (type == LocalDateTime.class) return LocalDateTime.parse(v);
            if (Enum.class.isAssignableFrom(type)) {
                return Enum.valueOf((Class<Enum>) type, v.toUpperCase());
            }
        } catch (Exception ignored) {}

        return v;
    }
}