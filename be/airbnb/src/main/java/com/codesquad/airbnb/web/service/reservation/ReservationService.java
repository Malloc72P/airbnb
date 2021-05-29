package com.codesquad.airbnb.web.service.reservation;

import com.codesquad.airbnb.web.domain.reservation.Reservation;
import com.codesquad.airbnb.web.domain.reservation.ReservationRepository;
import com.codesquad.airbnb.web.domain.room.Room;
import com.codesquad.airbnb.web.dto.ReservationPreview;
import com.codesquad.airbnb.web.dto.UserInput;
import com.codesquad.airbnb.web.exceptions.ReservationFailedException;
import com.codesquad.airbnb.web.exceptions.notfound.ReservationNotFoundException;
import com.codesquad.airbnb.web.service.rooms.RoomService;
import com.codesquad.airbnb.web.service.users.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codesquad.airbnb.web.exceptions.ReservationFailedException.RESERVATION_DATE_DUPLICATED;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationDtoConverter reservationDtoConverter;
    private final RoomService roomService;
    private final UserService userService;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationDtoConverter reservationDtoConverter,
                              RoomService roomService, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.reservationDtoConverter = reservationDtoConverter;
        this.roomService = roomService;
        this.userService = userService;
    }

    @Transactional
    public void cancelReservation(int reservationId, int guestId) {
        Reservation reservation = findReservation(reservationId);
        reservation.checkGuestIsOwner(guestId);
        reservationRepository.deleteReservation(reservationId);
    }

    public Reservation findReservation(int reservationId) {
        return reservationRepository.findReservationById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    @Transactional
    public ReservationPreview makeReservation(int roomId, int guestId, UserInput userInput) {
        userInput.verifyUserInputIsReservationable();
        Room room = roomService.findRoom(roomId);
        room.checkGuestCapacity(userInput.guestCount());
        checkUserIsExist(guestId);
        checkReservationable(roomId, userInput);
        Reservation reservation = createReservationInstance(roomId, guestId, userInput);
        save(reservation);
        return reservationDtoConverter.reservationToReservationDetail(reservation);
    }

    private void checkUserIsExist(int guestId) {
        userService.findGuest(guestId);
    }

    private Reservation createReservationInstance(int roomId, int guestId, UserInput userInput) {
        return Reservation.builder()
                .roomId(roomId)
                .guestId(guestId)
                .adultCount(userInput.getAdultCount())
                .childCount(userInput.getChildCount())
                .infantCount(userInput.getInfantCount())
                .checkinDateTime(userInput.getCheckIn())
                .checkoutDateTime(userInput.getCheckOut())
                .build();
    }

    public void checkReservationable(int roomId, UserInput userInput) {
        boolean result = reservationRepository.isReservationable(roomId, userInput);
        if (!result) {
            throw new ReservationFailedException(RESERVATION_DATE_DUPLICATED);
        }
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}
