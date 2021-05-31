package com.codesquad.airbnb.web.domain.room;

import com.codesquad.airbnb.web.dto.UserInput;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Room save(Room room);

    Optional<Room> findRoomById(int id);

    List<Room> findRoomsByUserInput(UserInput userInput);

    List<Integer> findPrices();
}
