package com.gundan.terragold.exception;

public class ForbiddenException extends BaseException {
    public ForbiddenException(String message) {
        super(message, "FORBIDDEN", null);
    }
}