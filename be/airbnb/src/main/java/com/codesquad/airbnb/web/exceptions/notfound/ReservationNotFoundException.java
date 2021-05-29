package com.codesquad.airbnb.web.exceptions.notfound;

public class ReservationNotFoundException extends NotFoundException {
    public static final String FIND_RESERVATION_BY_ID_FAILED = "예약을 찾을 수 없습니다 id : ";

    public ReservationNotFoundException(int id) {
        super(FIND_RESERVATION_BY_ID_FAILED + id);
    }
}
