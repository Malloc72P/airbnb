package com.codesquad.airbnb.web.domain.reservation;

import com.codesquad.airbnb.web.dto.UserInput;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    ReservationPreview save(ReservationPreview reservationPreview);

    Optional<ReservationPreview> findReservationById(int reservationId);

    List<ReservationDetail> findReservationsByGuestId(int guestId);

    boolean isReservationable(int roomId, UserInput userInput);

    void deleteReservation(int reservationId);
}
