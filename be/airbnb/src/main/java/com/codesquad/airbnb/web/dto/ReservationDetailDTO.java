package com.codesquad.airbnb.web.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReservationDetailDTO {
    private int id;
    private int guestId;
    private String roomName;
    private int pricePerDay;
    private int totalPrice;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private int adultCount;
    private int childCount;
    private int infantCount;
}
