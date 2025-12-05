package com.gundan.terragold.exception;

import com.gundan.terragold.util.ApiResponseBuilder;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiResponseBuilder api;

    private String traceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // -------------------------
    // BASE CUSTOM EXCEPTIONS
    // -------------------------
    @ExceptionHandler(BaseException.class)
    public Object handleBaseException(BaseException ex) {
        String trace = traceId();

        log.error("[{}] Business Exception: {}", trace, ex.getMessage());

        List<Map<String, String>> error = new ArrayList<>();
        error.add(Map.of(
                "title", ex.getErrorCode(),
                "message", ex.getMessage(),
                "trace", trace
        ));

        return api.buildErrorResponse(ex.getErrorCode(), ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // -------------------------
    // VALIDATION ERRORS
    // -------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationErrors(MethodArgumentNotValidException ex) {

        List<Map<String, String>> fieldErrors = new ArrayList<>();

        for (FieldError field : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.add(Map.of(
                    "title", "VALIDATION_ERROR",
                    "field", field.getField(),
                    "message", field.getDefaultMessage()
            ));
        }

        return api.buildResponse(
                null,
                null,
                null,
                null,
                List.of(fieldErrors),  // <-- nested list EXACTLY as your API spec requires
                null,
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(BindException.class)
    public Object handleBindException(BindException ex) {
        return handleValidationErrors(new MethodArgumentNotValidException(null, ex.getBindingResult()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraintViolation(ConstraintViolationException ex) {
        String trace = traceId();

        log.warn("[{}] Constraint violation: {}", trace, ex.getMessage());

        List<Map<String, String>> violations = new ArrayList<>();

        ex.getConstraintViolations().forEach(v -> {
            violations.add(Map.of(
                    "property", v.getPropertyPath().toString(),
                    "message", v.getMessage()
            ));
        });

        return api.buildErrorResponse(
                "CONSTRAINT_VIOLATION",
                violations.toString(),
                HttpStatus.BAD_REQUEST
        );
    }

    // -------------------------
    // JSON PARSING ERRORS
    // -------------------------
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleJsonParse(HttpMessageNotReadableException ex) {
        log.error("JSON parse error: {}", ex.getMessage());
        return api.buildErrorResponse(
                "INVALID_JSON",
                "Malformed JSON or wrong data type",
                HttpStatus.BAD_REQUEST
        );
    }

    // -------------------------
    // DATABASE ERRORS
    // -------------------------
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object handleDbConstraint(DataIntegrityViolationException ex) {
        log.error("DB constraint violation: {}", ex.getMessage());
        return api.buildErrorResponse(
                "DATABASE_CONSTRAINT",
                "A database constraint was violated",
                HttpStatus.CONFLICT
        );
    }

    // -------------------------
    // FALLBACK HANDLER
    // -------------------------
    @ExceptionHandler(Exception.class)
    public Object handleOther(Exception ex) {
        String trace = traceId();

        log.error("[{}] Unexpected Error: {}", trace, ex.getMessage(), ex);

        return api.buildErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Something went wrong. Trace ID: " + trace,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
