package com.codesquad.airbnb.web.service.reservation;

import com.codesquad.airbnb.web.domain.reservation.Reservation;
import com.codesquad.airbnb.web.dto.ReservationPreview;
import org.springframework.stereotype.Service;

@Service
public class ReservationDtoConverter {
    public ReservationPreview reservationToReservationDetail(Reservation reservation) {
        return ReservationPreview.builder()
                .id(reservation.getId())
                .roomId(reservation.getRoomId())
                .guestId(reservation.getGuestId())
                .adultCount(reservation.getAdultCount())
                .childCount(reservation.getChildCount())
                .infantCount(reservation.getInfantCount())
                .checkinDate(reservation.getCheckinDate())
                .checkoutDate(reservation.getCheckoutDate())
                .build();
    }
}
