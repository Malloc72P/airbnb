package com.codesquad.airbnb.web.service.reservation;

import com.codesquad.airbnb.web.domain.reservation.ReservationDetail;
import com.codesquad.airbnb.web.domain.reservation.ReservationPreview;
import com.codesquad.airbnb.web.domain.reservation.ReservationRepository;
import com.codesquad.airbnb.web.domain.room.Room;
import com.codesquad.airbnb.web.dto.ReservationDetailDTO;
import com.codesquad.airbnb.web.dto.ReservationPreviewDTO;
import com.codesquad.airbnb.web.dto.UserInput;
import com.codesquad.airbnb.web.exceptions.ReservationFailedException;
import com.codesquad.airbnb.web.exceptions.notfound.ReservationNotFoundException;
import com.codesquad.airbnb.web.service.rooms.RoomService;
import com.codesquad.airbnb.web.service.users.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        ReservationPreview reservationPreview = findReservation(reservationId);
        reservationPreview.checkGuestIsOwner(guestId);
        reservationRepository.deleteReservation(reservationId);
    }

    public ReservationPreview findReservation(int reservationId) {
        return reservationRepository.findReservationById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    public List<ReservationDetailDTO> showGuestsReservation(int guestId) {
        List<ReservationDetail> reservationPreviewList = reservationRepository.findReservationsByGuestId(guestId);
        return reservationPreviewList.stream()
                .map(reservationDtoConverter::reservationDetailToReservationDetailDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationPreviewDTO makeReservation(int roomId, int guestId, UserInput userInput) {
        userInput.verifyUserInputIsReservationable();
        Room room = roomService.findRoom(roomId);
        room.checkGuestCapacity(userInput.guestCount());
        checkUserIsExist(guestId);
        checkReservationable(roomId, userInput);
        ReservationPreview reservationPreview = createReservationInstance(roomId, guestId, userInput);
        save(reservationPreview);
        return reservationDtoConverter.reservationPreviewToReservationPreviewDTO(reservationPreview);
    }

    private void checkUserIsExist(int guestId) {
        userService.findGuest(guestId);
    }

    private ReservationPreview createReservationInstance(int roomId, int guestId, UserInput userInput) {
        return ReservationPreview.builder()
                .roomId(roomId)
                .guestId(guestId)
                .adultCount(userInput.getAdultCount())
                .childCount(userInput.getChildCount())
                .infantCount(userInput.getInfantCount())
                .checkinDate(userInput.getCheckIn())
                .checkoutDate(userInput.getCheckOut())
                .build();
    }

    public void checkReservationable(int roomId, UserInput userInput) {
        boolean result = reservationRepository.isReservationable(roomId, userInput);
        if (!result) {
            throw new ReservationFailedException(RESERVATION_DATE_DUPLICATED);
        }
    }

    public ReservationPreview save(ReservationPreview reservationPreview) {
        return reservationRepository.save(reservationPreview);
    }
}
