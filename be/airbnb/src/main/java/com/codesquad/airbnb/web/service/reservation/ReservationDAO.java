package com.codesquad.airbnb.web.service.reservation;

import com.codesquad.airbnb.web.domain.reservation.Reservation;
import com.codesquad.airbnb.web.domain.reservation.ReservationRepository;
import com.codesquad.airbnb.web.dto.UserInput;
import com.codesquad.airbnb.web.exceptions.InvalidSqlResultException;
import com.codesquad.airbnb.web.service.mapper.BooleanMapper;
import com.codesquad.airbnb.web.service.mapper.ReservationMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.codesquad.airbnb.web.exceptions.InvalidSqlResultException.RESERVATION_DUPLICATE_CHECK_ERROR;
import static com.codesquad.airbnb.web.statement.ReservationStatementKt.*;

@Service
public class ReservationDAO implements ReservationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ReservationMapper reservationMapper;
    private final BooleanMapper booleanMapper;

    public ReservationDAO(NamedParameterJdbcTemplate jdbcTemplate, ReservationMapper reservationMapper, BooleanMapper booleanMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationMapper = reservationMapper;
        this.booleanMapper = booleanMapper;
    }

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("room_id", reservation.getRoomId())
                .addValue("guest_id", reservation.getGuestId())
                .addValue("checkin_date_time", reservation.getCheckinDateTime())
                .addValue("checkout_date_time", reservation.getCheckoutDateTime())
                .addValue("adult_count", reservation.getAdultCount())
                .addValue("child_count", reservation.getChildCount())
                .addValue("infant_count", reservation.getInfantCount());
        jdbcTemplate.update(SAVE_RESERVATION, parameter, keyHolder);
        reservation.updateId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return reservation;
    }

    @Override
    public Optional<Reservation> findReservationById(int reservationId) {
        MapSqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("id", reservationId);
        try {
            Reservation reservation = jdbcTemplate.queryForObject(FIND_RESERVATION, parameter, reservationMapper);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isReservationable(int roomId, UserInput userInput) {
        MapSqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("room_id", roomId)
                .addValue("stay_start", userInput.getCheckIn())
                .addValue("stay_end", userInput.getCheckOut());
        Boolean result = jdbcTemplate.queryForObject(IS_RESERVATIONABLE, parameter, booleanMapper);
        if (result == null) {
            throw new InvalidSqlResultException(RESERVATION_DUPLICATE_CHECK_ERROR);
        }
        return result;
    }

    @Override
    public void deleteReservation(int reservationId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", reservationId);
        jdbcTemplate.update(DELETE_RESERVATION, parameterSource);
    }
}
