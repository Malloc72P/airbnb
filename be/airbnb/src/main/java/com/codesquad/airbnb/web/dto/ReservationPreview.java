package com.codesquad.airbnb.web.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReservationPreview {
    private int id;
    private int guestId;
    private int roomId;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private int adultCount;
    private int childCount;
    private int infantCount;
}
