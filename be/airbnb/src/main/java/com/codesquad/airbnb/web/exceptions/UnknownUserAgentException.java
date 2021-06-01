package com.codesquad.airbnb.web.exceptions;

public class UnknownUserAgentException extends RuntimeException{
    public static final String UNKNOWN_USER_AGENT = "식별할 수 없는 User Agent입니다";

    public UnknownUserAgentException(String message) {
        super(message);
    }
}
