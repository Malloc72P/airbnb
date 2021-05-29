package com.codesquad.airbnb.web.service.mapper;

import com.codesquad.airbnb.web.domain.reservation.Reservation;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class ReservationMapper implements RowMapper<Reservation> {
    @Override
    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Reservation.builder()
                .id(rs.getInt(1))
                .roomId(rs.getInt(2))
                .guestId(rs.getInt(3))
                .checkinDate(rs.getDate(4).toLocalDate())
                .checkoutDate(rs.getDate(5).toLocalDate())
                .adultCount(rs.getInt(6))
                .childCount(rs.getInt(7))
                .infantCount(rs.getInt(8))
                .build();
    }
}
