package com.codesquad.airbnb.web.controller;

import com.codesquad.airbnb.web.config.annotation.CertifiedUser;
import com.codesquad.airbnb.web.dto.ReservationPreview;
import com.codesquad.airbnb.web.dto.UserInput;
import com.codesquad.airbnb.web.service.reservation.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/rooms/{roomId}/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ReservationPreview makeReservation(@PathVariable int roomId,
                                              @RequestBody UserInput userInput,
                                              @CertifiedUser int userId) {
        return reservationService.makeReservation(roomId, userId, userInput);
    }

    @DeleteMapping("{reservationId}")
    public void deleteReservation(@PathVariable int roomId,
                                  @PathVariable int reservationId,
                                  @CertifiedUser int userId) {
        reservationService.cancelReservation(reservationId, userId);
    }
}
