package com.gundan.terragold.exception;

public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(message, "CONFLICT", null);
    }
}