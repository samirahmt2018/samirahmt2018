package com.gundan.terragold.exception;


import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final String errorCode;
    private final Object details;

    public BaseException(String message, String errorCode, Object details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }
}
