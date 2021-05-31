package com.codesquad.airbnb.web.service.rooms;

import com.codesquad.airbnb.web.domain.room.Room;
import com.codesquad.airbnb.web.domain.room.RoomRepository;
import com.codesquad.airbnb.web.dto.RoomDetail;
import com.codesquad.airbnb.web.dto.RoomPreviews;
import com.codesquad.airbnb.web.dto.UserInput;
import com.codesquad.airbnb.web.exceptions.notfound.RoomNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomDtoConverter roomDtoConverter;

    public RoomService(RoomRepository roomRepository, RoomDtoConverter roomDtoConverter) {
        this.roomRepository = roomRepository;
        this.roomDtoConverter = roomDtoConverter;
    }

    public RoomPreviews showRooms(UserInput userInput) {
        List<Room> rooms = roomRepository.findRoomsByUserInput(userInput);
        return roomDtoConverter.roomsToRoomPreviews(rooms, userInput.stayDay());
    }

    public RoomDetail showRoomDetail(int roomId, UserInput userInput) {
        Room room = findRoom(roomId);
        return roomDtoConverter.roomToRoomDetail(room, userInput.stayDay());
    }

    public Room findRoom(int roomId) {
        return roomRepository.findRoomById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
    }

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public HashMap<Integer, Integer> showPriceRange() {
        List<Integer> prices = roomRepository.findPrices();
        HashMap<Integer, Integer> priceMap = new LinkedHashMap<>();
        for (Integer price : prices) {
            int key = calculateKey(price);
            int count = 1;
            if (priceMap.containsKey(key)) {
                count = priceMap.get(key) + 1;
            }
            priceMap.put(key, count);
        }
        return priceMap;
    }

    private int calculateKey(int price) {
        int priceHeader = price / 10000;
        return priceHeader * 10000;
    }
}
