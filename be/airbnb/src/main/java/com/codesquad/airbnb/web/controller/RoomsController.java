package com.codesquad.airbnb.web.controller;

import com.codesquad.airbnb.web.dto.RoomDetail;
import com.codesquad.airbnb.web.dto.RoomPreviews;
import com.codesquad.airbnb.web.dto.UserInput;
import com.codesquad.airbnb.web.service.rooms.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/rooms")
public class RoomsController {

    private final RoomService roomService;

    public RoomsController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public RoomPreviews rooms(UserInput userInput) {
        return roomService.showRooms(userInput);
    }

    @GetMapping("/{roomId}")
    public RoomDetail roomDetail(@PathVariable int roomId, UserInput userInput) {
        return roomService.showRoomDetail(roomId, userInput);
    }

    @GetMapping("/prices")
    public HashMap<Integer, Integer> showPriceRange() {
        return roomService.showPriceRange();
    }
}
