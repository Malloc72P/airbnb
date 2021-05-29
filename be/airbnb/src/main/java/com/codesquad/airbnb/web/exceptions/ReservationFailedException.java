package com.codesquad.airbnb.web.exceptions;

public class ReservationFailedException extends RuntimeException {
    public static final String RESERVATION_DATE_DUPLICATED = "해당 기간에 예약할 수 없습니다. 기간이 겹칩니다";
    public static final String GUEST_CAPACITY_EXCEED = "숙박인원이 숙소의 수용가능인원을 초과했습니다";
    public static final String CANCELATION_FAILED_GUEST_IS_NOT_OWNER = "다른 사람의 예약정보입니다. 자신의 예약만 취소할 수 있습니다.";

    public ReservationFailedException(String message) {
        super(message);
    }
}
