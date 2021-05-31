package com.codesquad.airbnb.web.service.rooms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @Test
    @DisplayName("가격범위를 조회할 수 있어야 합니다")
    void testPriceRange() {
        roomService.showPriceRange();
    }
}
