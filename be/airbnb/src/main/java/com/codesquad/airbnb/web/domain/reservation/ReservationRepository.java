package com.codesquad.airbnb.web.domain.reservation;

import com.codesquad.airbnb.web.dto.UserInput;

import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Optional<Reservation> findReservationById(int reservationId);

    boolean isReservationable(int roomId, UserInput userInput);

    void deleteReservation(int reservationId);
}
