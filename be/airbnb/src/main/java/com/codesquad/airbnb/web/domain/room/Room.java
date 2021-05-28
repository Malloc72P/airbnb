package com.codesquad.airbnb.web.domain.room;

import com.codesquad.airbnb.web.domain.user.Host;
import com.codesquad.airbnb.web.exceptions.ReservationFailedException;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.List;

import static com.codesquad.airbnb.web.exceptions.ReservationFailedException.GUEST_CAPACITY_EXCEED;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Room {
    private int id;
    private int locationId;
    private String locationName;
    private String name;
    private float rating;
    private int guestCapacity;
    private Point point;
    private String description;
    private BedroomType bedroomType;
    private int bedCount;
    private BathroomType bathroomType;
    private PricePolicy pricePolicy;
    private String amenity;
    private int reviewCount;
    private String thumbnail;
    private Host host;
    @Builder.Default
    private List<RoomImage> detailImages = new ArrayList<>();

    public void updateId(int id) {
        this.id = id;
    }

    public Room addImage(RoomImage image) {
        detailImages.add(image);
        return this;
    }

    public void addImages(List<RoomImage> images) {
        detailImages.addAll(images);
    }

    public void checkGuestCapacity(int guestCount) {
        if (guestCapacity < guestCount) {
            throw new ReservationFailedException(GUEST_CAPACITY_EXCEED);
        }
    }
}
