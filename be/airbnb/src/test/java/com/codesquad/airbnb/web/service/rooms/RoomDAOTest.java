package com.codesquad.airbnb.web.service.rooms;

import com.codesquad.airbnb.web.domain.room.*;
import com.codesquad.airbnb.web.domain.user.Host;
import com.codesquad.airbnb.web.dto.UserInput;
import com.codesquad.airbnb.web.exceptions.notfound.RoomNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.codesquad.airbnb.web.dto.UserInput.DATE_FORMATTER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RoomDAOTest {

    @Autowired
    private RoomDAO roomDAO;

    @Test
    @DisplayName("숙소정보를 저장하고 조회할 수 있어야 함")
    void testSaveRoom() {
        Room room = createRoom();
        roomDAO.save(room);
        final int roomId = room.getId();
        Room testRoom = roomDAO.findRoomById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
        verifyRoom(testRoom, room);
    }

    private void verifyRoom(Room testTarget, Room expected) {
        assertThat(testTarget)
                .extracting(
                        Room::getName,
                        Room::getDescription,
                        Room::getGuestCapacity,
                        Room::getLocationId,
                        Room::getPoint,
                        Room::getRating,
                        Room::getBathroomType,
                        Room::getBedroomType,
                        Room::getBedCount,
                        Room::getAmenity,
                        Room::getLocationName,
                        Room::getThumbnail,
                        Room::getReviewCount)
                .doesNotContainNull()
                .containsExactly(
                        expected.getName(),
                        expected.getDescription(),
                        expected.getGuestCapacity(),
                        expected.getLocationId(),
                        expected.getPoint(),
                        expected.getRating(),
                        expected.getBathroomType(),
                        expected.getBedroomType(),
                        expected.getBedCount(),
                        expected.getAmenity(),
                        expected.getLocationName(),
                        expected.getThumbnail(),
                        expected.getReviewCount()
                );
    }

    @Test
    @DisplayName("숙소 가격정보를 저장하고 조회할 수 있어야 함")
    void testPricePolicy() {
        Room room = createRoom();
        roomDAO.save(room);
        final int roomId = room.getId();
        Room testRoom = roomDAO.findRoomById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
        assertThat(testRoom).isNotNull();
        PricePolicy pricePolicy = testRoom.getPricePolicy();
        PricePolicy expected = room.getPricePolicy();
        verifyPricePolicy(pricePolicy, expected);
    }

    private void verifyPricePolicy(PricePolicy target, PricePolicy expected) {
        assertThat(target)
                .extracting(
                        PricePolicy::getPricePerDay,
                        PricePolicy::getAccomodationTax,
                        PricePolicy::getCleanUpCost,
                        PricePolicy::getServiceFee,
                        PricePolicy::getWeeklyDiscount
                )
                .doesNotContainNull()
                .containsExactly(
                        expected.getPricePerDay(),
                        expected.getAccomodationTax(),
                        expected.getCleanUpCost(),
                        expected.getServiceFee(),
                        expected.getWeeklyDiscount()
                );
    }

    @Test
    @DisplayName("숙소 호스트정보를 저장하고 조회할 수 있어야 함")
    void testHost() {
        Room room = createRoom();
        roomDAO.save(room);
        final int roomId = room.getId();
        Room testRoom = roomDAO.findRoomById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
        assertThat(testRoom).isNotNull();
        Host target = testRoom.getHost();
        Host expected = Host.builder()
                .id(1)
                .name("Milo Kachinsky")
                .isSuperhost(true)
                .profileImage("https://static.wikia.nocookie.net/starcraft/images/d/d5/Kachinsky_SC2_Head1.jpg/revision/latest/top-crop/width/360/height/360?cb=20100722105327")
                .build();
        verifyHost(target, expected);
    }

    private void verifyHost(Host target, Host expected) {
        assertThat(target)
                .extracting(
                        Host::getId,
                        Host::getName,
                        Host::isSuperhost,
                        Host::getProfileImage
                )
                .doesNotContainNull()
                .containsExactly(
                        expected.getId(),
                        expected.getName(),
                        expected.isSuperhost(),
                        expected.getProfileImage()
                );
    }

    @Test
    @DisplayName("숙소 이미지정보를 저장하고 조회할 수 있어야 함")
    void testImages() {
        Room room = createRoom();
        roomDAO.save(room);
        final int roomId = room.getId();
        Room testRoom = roomDAO.findRoomById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
        assertThat(testRoom).isNotNull();
        verifyImages(testRoom.getDetailImages(), room.getDetailImages());
    }

    private void verifyImages(List<RoomImage> target, List<RoomImage> expected) {
        for (int i = 0; i < target.size(); i++) {
            RoomImage targetImage = target.get(i);
            RoomImage expectedImage = expected.get(i);
            assertThat(targetImage)
                    .extracting(
                            RoomImage::getUrl,
                            RoomImage::getIndex
                    )
                    .doesNotContainNull()
                    .containsExactly(
                            expectedImage.getUrl(),
                            expectedImage.getIndex()
                    );
        }
    }

    @Test
    @DisplayName("숙소를 UserInput으로 조회할 수 있어야 함")
    void searchRooms() {
        Room room = createRoom();
        roomDAO.save(room);
        UserInput userInput = UserInput.builder()
                .location("서울특별시")
                .checkIn(LocalDate.parse("2021-05-01", DATE_FORMATTER))
                .checkOut(LocalDate.parse("2021-05-02", DATE_FORMATTER))
                .priceMinimum(1)
                .priceMaximum(10_000_000)
                .adultCount(1)
                .childCount(0)
                .infantCount(0)
                .build();
        List<Room> rooms = roomDAO.findRoomsByUserInput(userInput);
        assertThat(rooms.size() > 0).isTrue();
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
        room.addImage(new RoomImage("https://a0.muscache.com/im/pictures/02ebf802-8599-4b9b-af42-d45eeea7989d.jpg?im_w=1200", 1))
                .addImage(new RoomImage("https://a0.muscache.com/im/pictures/f6e9a07a-8a17-4d14-8a42-4fc3e3b5f2bd.jpg?im_w=720", 2))
                .addImage(new RoomImage("https://a0.muscache.com/im/pictures/cc2371de-4386-4f2c-80ce-763ca32bc058.jpg?im_w=720", 3))
                .addImage(new RoomImage("https://a0.muscache.com/im/pictures/56cccaca-04fd-48d6-9877-13f41b037cab.jpg?im_w=1200", 4))
                .addImage(new RoomImage("https://a0.muscache.com/im/pictures/5a02e1f5-042b-4158-abbf-5396756e7f68.jpg?im_w=720", 5));
        return room;
    }
}
