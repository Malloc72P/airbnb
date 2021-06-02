package com.codesquad.airbnb.web.service.mapper;

import com.codesquad.airbnb.web.domain.reservation.ReservationDetail;
import com.codesquad.airbnb.web.domain.room.PricePolicy;
import com.codesquad.airbnb.web.domain.room.Room;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class ReservationDetailMapper implements RowMapper<ReservationDetail> {
    @Override
    public ReservationDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ReservationDetail.builder()
                .id(rs.getInt(1))
                .guestId(rs.getInt(2))
                .checkinDate(rs.getDate(3).toLocalDate())
                .checkoutDate(rs.getDate(4).toLocalDate())
                .adultCount(rs.getInt(5))
                .childCount(rs.getInt(6))
                .infantCount(rs.getInt(7))
                .room(Room.builder()
                        .id(rs.getInt(8))
                        .name(rs.getString(9))
                        .pricePolicy(PricePolicy.builder()
                                .accomodationTax(rs.getInt(10))
                                .cleanUpCost(rs.getInt(11))
                                .pricePerDay(rs.getInt(12))
                                .serviceFee(rs.getInt(13))
                                .weeklyDiscount(rs.getInt(14))
                                .build())
                        .build())
                .build();
    }
}
