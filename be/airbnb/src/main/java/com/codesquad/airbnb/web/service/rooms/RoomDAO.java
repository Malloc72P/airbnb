package com.codesquad.airbnb.web.service.rooms;

import com.codesquad.airbnb.web.domain.room.PricePolicy;
import com.codesquad.airbnb.web.domain.room.Room;
import com.codesquad.airbnb.web.domain.room.RoomImage;
import com.codesquad.airbnb.web.domain.room.RoomRepository;
import com.codesquad.airbnb.web.dto.UserInput;
import com.codesquad.airbnb.web.service.mapper.RoomImageMapper;
import com.codesquad.airbnb.web.service.mapper.RoomMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.codesquad.airbnb.web.statement.RoomStatementKt.*;

@Service
public class RoomDAO implements RoomRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RoomMapper roomMapper;
    private final RoomImageMapper roomImageMapper;

    public RoomDAO(NamedParameterJdbcTemplate jdbcTemplate, RoomMapper roomMapper, RoomImageMapper roomImageMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.roomMapper = roomMapper;
        this.roomImageMapper = roomImageMapper;
    }

    @Override
    public Room save(Room room) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PricePolicy pricePolicy = room.getPricePolicy();
        MapSqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("location_id", room.getLocationId())
                .addValue("name", room.getName())
                .addValue("rating", room.getRating())
                .addValue("guest_capacity", room.getGuestCapacity())
                .addValue("x", room.getPoint().getX())
                .addValue("y", room.getPoint().getY())
                .addValue("description", room.getDescription())
                .addValue("bathroom_type", room.getBathroomType().name())
                .addValue("bedroom_type", room.getBedroomType().name())
                .addValue("bed_count", room.getBedCount())
                .addValue("amenity", room.getAmenity())
                .addValue("review_count", room.getReviewCount())
                .addValue("thumbnail", room.getThumbnail())
                .addValue("host_id", room.getHost().getId())
                .addValue("service_fee", pricePolicy.getServiceFee())
                .addValue("accomodation_tax", pricePolicy.getAccomodationTax())
                .addValue("clean_up_cost", pricePolicy.getCleanUpCost())
                .addValue("price_per_day", pricePolicy.getPricePerDay())
                .addValue("weekly_discount", pricePolicy.getWeeklyDiscount());
        jdbcTemplate.update(SAVE_ROOM, parameter, keyHolder);
        room.updateId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        saveRoomImages(room);
        return room;
    }

    private void saveRoomImages(Room room) {
        MapSqlParameterSource[] parameters = room.getDetailImages().stream()
                .map(image -> new MapSqlParameterSource()
                        .addValue("room_id", room.getId())
                        .addValue("image_url", image.getUrl())
                        .addValue("image_index", image.getIndex()))
                .toArray(MapSqlParameterSource[]::new);
        jdbcTemplate.batchUpdate(SAVE_IMAGE, parameters);
    }

    @Override
    public Optional<Room> findRoomById(int id) {
        MapSqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            Room room = jdbcTemplate.queryForObject(FIND_ROOM, parameter, roomMapper);
            if (room == null) {
                return Optional.empty();
            }
            List<RoomImage> roomImages = findRoomImages(room.getId());
            room.addImages(roomImages);
            return Optional.of(room);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private List<RoomImage> findRoomImages(int roomId) {
        MapSqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("room_id", roomId);
        return jdbcTemplate.query(FIND_IMAGES, parameter, roomImageMapper);
    }

    @Override
    public List<Room> findRoomsByUserInput(UserInput userInput) {
        userInput.checkLocationAvailable();
        StringBuilder sqlBuilder = new StringBuilder().append(SEARCH_ROOMS_BY_LOCATION);
        MapSqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("location_name", userInput.lastLocation());
        addCheckinFilter(userInput, sqlBuilder, parameter);
        addGuestCountFilter(userInput, sqlBuilder, parameter);
        addPriceRange(userInput, sqlBuilder, parameter);
        sqlBuilder.append(";");
        return jdbcTemplate.query(sqlBuilder.toString(), parameter, roomMapper);
    }

    private void addCheckinFilter(UserInput userInput, StringBuilder sqlBuilder, MapSqlParameterSource parameter) {
        if (userInput.checkStayDurationAvailable()) {
            sqlBuilder.append(FILTERING_DATE);
            parameter.addValue("stay_start", userInput.getCheckIn())
                    .addValue("stay_end", userInput.getCheckOut());
        }
    }

    private void addGuestCountFilter(UserInput userInput, StringBuilder sqlBuilder, MapSqlParameterSource parameter) {
        if (userInput.checkGuestCountAvailable()) {
            sqlBuilder.append(FILTERING_GUEST_COUNT);
            parameter.addValue("guest_count", userInput.guestCount());
        }
    }

    private void addPriceRange(UserInput userInput, StringBuilder sqlBuilder, MapSqlParameterSource parameter) {
        if (userInput.checkPriceRangeAvailable()) {
            sqlBuilder.append(FILTERING_PRICE);
            parameter.addValue("cost_minimum", userInput.getPriceMinimum())
                    .addValue("cost_maximum", userInput.getPriceMaximum());
        }
    }
}
