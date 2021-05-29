package com.codesquad.airbnb.web.dto;

import com.codesquad.airbnb.web.exceptions.InvalidUserInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.codesquad.airbnb.web.dto.UserInput.DATE_FORMATTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserInputTest {

    @Test
    @DisplayName("숙박기간 테스트")
    void testStayDay() {
        testStayDay("2021-05-01", "2021-05-02", 1);
        testStayDay("2021-05-01", "2021-05-07", 6);
        testStayDay("2021-05-01", "2021-06-03", 33);
    }

    private void testStayDay(String checkIn, String checkOut, int stay) {
        UserInput userInput = UserInput.builder()
                .checkIn(LocalDate.parse(checkIn, DATE_FORMATTER))
                .checkOut(LocalDate.parse(checkOut, DATE_FORMATTER))
                .build();
        long stayDay = userInput.stayDay();
        assertThat(stayDay).isEqualTo(stay);
    }

    @Test
    @DisplayName("숙박기간 에러 테스트")
    public void testErrorStayDay() {
        UserInput userInput = UserInput.builder()
                .checkIn(LocalDate.parse("2021-05-01", DATE_FORMATTER))
                .checkOut(LocalDate.parse("2021-04-01", DATE_FORMATTER))
                .build();
        assertThatThrownBy(userInput::checkStayDurationAvailable)
                .isInstanceOf(InvalidUserInputException.class)
                .hasMessageContaining(InvalidUserInputException.NO_STAY_DAYS);
    }

    @Test
    @DisplayName("숙박일 정보가 없으면 false를 반환해야함")
    void testHasCheckInAndOut1() {
        UserInput userInput = UserInput.builder()
                .build();
        assertThat(userInput.checkStayDurationAvailable()).isFalse();
    }

    @Test
    @DisplayName("숙박일 정보가 결손되었다면 false를 반환해야함")
    void testHasCheckInAndOut2() {
        UserInput userInput = UserInput.builder()
                .checkIn(LocalDate.parse("2021-05-01"))
                .build();
        assertThat(userInput.checkStayDurationAvailable()).isFalse();
    }

    @Test
    @DisplayName("숙박인원 정보가 결손되었다면 false를 반환해야함")
    void testHasGuest3() {
        UserInput userInput = UserInput.builder()
                .adultCount(2)
                .build();
        assertThat(userInput.checkGuestCountAvailable()).isFalse();
    }

    @Test
    @DisplayName("숙박인원수를 구할 수 있어야 함")
    void testHasGuestCount() {
        UserInput userInput = UserInput.builder()
                .adultCount(2)
                .childCount(0)
                .infantCount(0)
                .build();
        assertThat(userInput.checkGuestCountAvailable()).isTrue();
        assertThat(userInput.guestCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("가격범위정보가 결손되었다면 false를 반환해야함")
    void testHasPriceRange1() {
        UserInput userInput = UserInput.builder()
                .priceMinimum(100)
                .build();
        assertThat(userInput.checkPriceRangeAvailable()).isFalse();
    }

    @Test
    @DisplayName("가격범위가 전부 제공되면 true를 반환해야함")
    void testHasPriceRange2() {
        UserInput userInput = UserInput.builder()
                .priceMinimum(1000)
                .priceMaximum(10000)
                .build();
        assertThat(userInput.checkPriceRangeAvailable()).isTrue();
    }

    @Test
    @DisplayName("가격범위에 에러가 있으면 예외가 발생해야 함")
    void testPriceRangeError() {
        UserInput userInput = UserInput.builder()
                .priceMinimum(10000)
                .priceMaximum(1000)
                .build();
        assertThatThrownBy(userInput::checkPriceRangeAvailable)
                .isInstanceOf(InvalidUserInputException.class)
                .hasMessageContaining(InvalidUserInputException.PRICE_RANGE_ERROR);
    }
}
