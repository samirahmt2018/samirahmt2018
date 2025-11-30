package com.gundan.terragold.exception;

import com.gundan.terragold.util.ApiResponse;
import com.gundan.terragold.util.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiResponseBuilder api;

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception ex) {
        return api.buildErrorResponse(
                "Internal Server Error",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
