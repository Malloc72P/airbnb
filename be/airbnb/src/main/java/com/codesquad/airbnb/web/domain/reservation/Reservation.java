package com.codesquad.airbnb.web.domain.reservation;

import com.codesquad.airbnb.web.exceptions.ReservationFailedException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

import static com.codesquad.airbnb.web.exceptions.ReservationFailedException.CANCELATION_FAILED_GUEST_IS_NOT_OWNER;

@Getter
@Builder
public class Reservation {
    private int id;
    private int guestId;
    private int roomId;
    private LocalDate checkinDateTime;
    private LocalDate checkoutDateTime;
    private int adultCount;
    private int childCount;
    private int infantCount;

    public void updateId(int newId) {
        this.id = newId;
    }

    public void checkGuestIsOwner(int guestId) {
        boolean isOwner = guestId == this.guestId;
        if(!isOwner) {
            throw new ReservationFailedException(CANCELATION_FAILED_GUEST_IS_NOT_OWNER);
        }
    }
}
