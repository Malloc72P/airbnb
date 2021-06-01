package com.codesquad.airbnb.web.exceptions;

public class UnknownUserAgentException extends RuntimeException{
    public static final String UNKNOWN_USER_AGENT = "식별할 수 없는 User Agent입니다";
    public static final String NO_USER_AGENT = "User Agent정보가 요청 헤더에 없습니다";

    public UnknownUserAgentException(String message) {
        super(message);
    }
}
