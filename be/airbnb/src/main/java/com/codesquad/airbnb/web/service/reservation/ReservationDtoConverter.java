package com.codesquad.airbnb.web.service.reservation;

import com.codesquad.airbnb.web.domain.reservation.ReservationDetail;
import com.codesquad.airbnb.web.domain.reservation.ReservationPreview;
import com.codesquad.airbnb.web.domain.room.PricePolicy;
import com.codesquad.airbnb.web.domain.room.Room;
import com.codesquad.airbnb.web.dto.ReservationDetailDTO;
import com.codesquad.airbnb.web.dto.ReservationPreviewDTO;
import org.springframework.stereotype.Service;

@Service
public class ReservationDtoConverter {
    public ReservationPreviewDTO reservationPreviewToReservationPreviewDTO(ReservationPreview reservationPreview) {
        return ReservationPreviewDTO.builder()
                .id(reservationPreview.getId())
                .roomId(reservationPreview.getRoomId())
                .guestId(reservationPreview.getGuestId())
                .adultCount(reservationPreview.getAdultCount())
                .childCount(reservationPreview.getChildCount())
                .infantCount(reservationPreview.getInfantCount())
                .checkinDate(reservationPreview.getCheckinDate())
                .checkoutDate(reservationPreview.getCheckoutDate())
                .build();
    }

    public ReservationDetailDTO reservationDetailToReservationDetailDTO(ReservationDetail reservation) {
        Room room = reservation.getRoom();
        PricePolicy pricePolicy = room.getPricePolicy();
        return ReservationDetailDTO.builder()
                .id(reservation.getId())
                .roomName(room.getName())
                .pricePerDay(pricePolicy.getPricePerDay())
                .totalPrice(pricePolicy.totalPrice(reservation.stayDay()))
                .guestId(reservation.getGuestId())
                .adultCount(reservation.getAdultCount())
                .childCount(reservation.getChildCount())
                .infantCount(reservation.getInfantCount())
                .checkinDate(reservation.getCheckinDate())
                .checkoutDate(reservation.getCheckoutDate())
                .build();
    }
}
