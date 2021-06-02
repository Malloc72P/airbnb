package com.codesquad.airbnb.web.controller;

import com.codesquad.airbnb.web.config.annotation.CertifiedUser;
import com.codesquad.airbnb.web.dto.ReservationDetailDTO;
import com.codesquad.airbnb.web.service.reservation.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class GuestController {
    private final ReservationService reservationService;

    public GuestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationDetailDTO> showGuestsReservation(@CertifiedUser int guestId) {
        return reservationService.showGuestsReservation(guestId);
    }
}
