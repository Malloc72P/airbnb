package com.codesquad.airbnb.web.service.reservation;

import com.codesquad.airbnb.web.domain.reservation.Reservation;
import com.codesquad.airbnb.web.domain.room.BathroomType;
import com.codesquad.airbnb.web.domain.room.BedroomType;
import com.codesquad.airbnb.web.domain.room.PricePolicy;
import com.codesquad.airbnb.web.domain.room.Room;
import com.codesquad.airbnb.web.domain.user.Host;
import com.codesquad.airbnb.web.dto.ReservationPreview;
import com.codesquad.airbnb.web.dto.UserInput;
import com.codesquad.airbnb.web.exceptions.notfound.ReservationNotFoundException;
import com.codesquad.airbnb.web.service.rooms.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.codesquad.airbnb.web.dto.UserInput.DATE_FORMATTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    private static final int TEST_GUEST_ID = 2;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @Test
    @DisplayName("숙소를 예약할 수 있어야 함")
    void testMakeReservation() {
        UserInput userInput = createUserInput();
        Reservation reservation = makeReservation(userInput);
        testReservation(userInput, reservation);
    }

    private void testReservation(UserInput userInput, Reservation reservation) {
        assertThat(reservation)
                .extracting(Reservation::getCheckinDate,
                        Reservation::getCheckoutDate,
                        Reservation::getAdultCount,
                        Reservation::getChildCount,
                        Reservation::getInfantCount)
                .doesNotContainNull()
                .containsExactly(userInput.getCheckIn(),
                        userInput.getCheckOut(),
                        userInput.getAdultCount(),
                        userInput.getChildCount(),
                        userInput.getInfantCount());
    }

    @Test
    @DisplayName("숙소예약을 취소할 수 있어야 함")
    void testReservationCancelation() {
        UserInput userInput = createUserInput();
        Reservation reservation = makeReservation(userInput);
        reservationService.cancelReservation(reservation.getId(), TEST_GUEST_ID);
        testReservationIsCanceled(reservation);
    }

    private void testReservationIsCanceled(Reservation reservation) {
        assertThatThrownBy(() -> reservationService.findReservation(reservation.getId()))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessageContaining(ReservationNotFoundException.FIND_RESERVATION_BY_ID_FAILED);
    }

    private Reservation makeReservation(UserInput userInput) {
        Room room = createRoom();
        ReservationPreview reservationPreview = reservationService.makeReservation(room.getId(), TEST_GUEST_ID, userInput);
        Reservation reservation = reservationService.findReservation(reservationPreview.getId());
        assertThat(reservation).isNotNull();
        return reservation;
    }

    private UserInput createUserInput() {
        return UserInput.builder()
                .checkIn(LocalDate.parse("2021-05-01", DATE_FORMATTER))
                .checkOut(LocalDate.parse("2021-05-02", DATE_FORMATTER))
                .adultCount(1)
                .childCount(0)
                .infantCount(0)
                .build();
    }

    private Room createRoom() {
        String name = "aaaa";
        String description = "bbbb";
        int guestCapacity = 2;
        int locationId = 4;
        String locationName = "오금동";
        Point point = new Point(37.252352, 235.52532);
        float rating = 0.5f;
        Room room = Room.builder()
                .name(name)
                .description(description)
                .guestCapacity(guestCapacity)
                .locationId(locationId)
                .locationName(locationName)
                .point(point)
                .rating(rating)
                .bathroomType(BathroomType.PRIVATE_BATHROOM)
                .bedroomType(BedroomType.BEDROOM)
                .bedCount(2)
                .amenity("주방, 무선인터넷, 에어컨, 헤어드라이어")
                .pricePolicy(PricePolicy.builder()
                        .pricePerDay(50000)
                        .weeklyDiscount(5)
                        .serviceFee(1000)
                        .cleanUpCost(5000)
                        .accomodationTax(5000)
                        .build())
                .reviewCount(123)
                .thumbnail("https://pix10.agoda.net/hotelImages/124/1246280/1246280_16061017110043391702.jpg?s=1024x768")
                .host(Host.builder().id(1).build())
                .build();
        return roomService.saveRoom(room);
    }
}
