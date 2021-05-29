package com.codesquad.airbnb.web.dto;

import com.codesquad.airbnb.web.exceptions.InvalidUserInputException;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.codesquad.airbnb.web.exceptions.InvalidUserInputException.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserInput {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String STRING_DATE_TIME_FORMAT = "yyyy-MM-dd";
    private static final int DEFAULT_GUEST_COUNT = 1;

    private String location;
    @DateTimeFormat(pattern = STRING_DATE_TIME_FORMAT)
    private LocalDate checkIn;
    @DateTimeFormat(pattern = STRING_DATE_TIME_FORMAT)
    private LocalDate checkOut;
    private Integer adultCount;
    private Integer childCount;
    private Integer infantCount;
    private Integer priceMinimum;
    private Integer priceMaximum;

    public void checkLocationAvailable() {
        boolean isAvailable = location != null && !location.isEmpty();
        if (!isAvailable) {
            throw new InvalidUserInputException(NO_LOCATION_INFORMATION);
        }
    }

    public boolean checkStayDurationAvailable() {
        if (isStayDurationNull()) {
            return false;
        }
        long days = checkIn.until(checkOut, ChronoUnit.DAYS);
        if (days <= 0) {
            throw new InvalidUserInputException(NO_STAY_DAYS);
        }
        return true;
    }

    private boolean isStayDurationNull() {
        return checkIn == null || checkOut == null;
    }

    public boolean checkGuestCountAvailable() {
        return adultCount != null && childCount != null && infantCount != null;
    }

    public boolean checkPriceRangeAvailable() {
        boolean isNotNull = priceMinimum != null && priceMaximum != null;
        if (!isNotNull) {
            return false;
        }
        if (priceMinimum > priceMaximum) {
            throw new InvalidUserInputException(PRICE_RANGE_ERROR);
        }
        return true;
    }

    public void verifyUserInputIsReservationable() {
        checkGuestCountAvailable();
        checkStayDurationAvailable();
    }

    public int guestCount() {
        if (checkGuestCountAvailable()) {
            return adultCount + childCount + infantCount;
        }
        return DEFAULT_GUEST_COUNT;
    }

    public int stayDay() {
        if (checkStayDurationAvailable()) {
            return Math.toIntExact(checkIn.until(checkOut, ChronoUnit.DAYS));
        } else return 1;
    }

    public String lastLocation() {
        String[] locations = location.split(" ");
        return locations[locations.length - 1];
    }
}
