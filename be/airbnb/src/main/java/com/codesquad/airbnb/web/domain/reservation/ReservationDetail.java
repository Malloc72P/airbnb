package com.codesquad.airbnb.web.domain.reservation;

import com.codesquad.airbnb.web.domain.room.Room;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Builder
public class ReservationDetail {
    private int id;
    private int guestId;
    private Room room;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private int adultCount;
    private int childCount;
    private int infantCount;

    public int stayDay() {
        return Math.toIntExact(checkinDate.until(checkoutDate, ChronoUnit.DAYS));
    }
}
