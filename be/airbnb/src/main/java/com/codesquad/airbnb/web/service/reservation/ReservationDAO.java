package com.codesquad.airbnb.web.service.reservation;

import com.codesquad.airbnb.web.domain.reservation.ReservationDetail;
import com.codesquad.airbnb.web.domain.reservation.ReservationPreview;
import com.codesquad.airbnb.web.domain.reservation.ReservationRepository;
import com.codesquad.airbnb.web.dto.UserInput;
import com.codesquad.airbnb.web.exceptions.InvalidSqlResultException;
import com.codesquad.airbnb.web.service.mapper.BooleanMapper;
import com.codesquad.airbnb.web.service.mapper.ReservationDetailMapper;
import com.codesquad.airbnb.web.service.mapper.ReservationPreviewMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.codesquad.airbnb.web.exceptions.InvalidSqlResultException.RESERVATION_DUPLICATE_CHECK_ERROR;
import static com.codesquad.airbnb.web.statement.ReservationStatementKt.*;

@Service
public class ReservationDAO implements ReservationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ReservationPreviewMapper reservationPreviewMapper;
    private final ReservationDetailMapper reservationDetailMapper;
    private final BooleanMapper booleanMapper;

    public ReservationDAO(NamedParameterJdbcTemplate jdbcTemplate,
                          ReservationPreviewMapper reservationPreviewMapper,
                          ReservationDetailMapper reservationDetailMapper,
                          BooleanMapper booleanMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationPreviewMapper = reservationPreviewMapper;
        this.reservationDetailMapper = reservationDetailMapper;
        this.booleanMapper = booleanMapper;
    }

    @Override
    public ReservationPreview save(ReservationPreview reservationPreview) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("room_id", reservationPreview.getRoomId())
                .addValue("guest_id", reservationPreview.getGuestId())
                .addValue("checkin_date", reservationPreview.getCheckinDate())
                .addValue("checkout_date", reservationPreview.getCheckoutDate())
                .addValue("adult_count", reservationPreview.getAdultCount())
                .addValue("child_count", reservationPreview.getChildCount())
                .addValue("infant_count", reservationPreview.getInfantCount());
        jdbcTemplate.update(SAVE_RESERVATION, parameter, keyHolder);
        reservationPreview.updateId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return reservationPreview;
    }

    @Override
    public Optional<ReservationPreview> findReservationById(int reservationId) {
        MapSqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("id", reservationId);
        try {
            ReservationPreview reservationPreview = jdbcTemplate.queryForObject(FIND_RESERVATION, parameter, reservationPreviewMapper);
            return Optional.ofNullable(reservationPreview);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationDetail> findReservationsByGuestId(int guestId) {
        MapSqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("guest_id", guestId);
        return jdbcTemplate.query(FIND_RESERVATION_BY_GUEST_ID, parameter, reservationDetailMapper);
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
