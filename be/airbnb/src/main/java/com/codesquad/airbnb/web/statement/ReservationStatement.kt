package com.codesquad.airbnb.web.statement

const val SAVE_RESERVATION: String = """
insert into reservation(room_id, guest_id, checkin_date_time, checkout_date_time, adult_count, child_count,infant_count)
values (:room_id, :guest_id, :checkin_date_time, :checkout_date_time, :adult_count, :child_count,:infant_count);
"""

const val FIND_RESERVATION: String = """
select id, room_id, guest_id, checkin_date_time, checkout_date_time, adult_count, child_count,infant_count
from reservation
where id = :id;
"""

const val IS_RESERVATIONABLE: String = """
select if(count(id) = 0, true, false) reservationable
from reservation rsv
where rsv.room_id = :room_id
  and rsv.checkin_date_time < :stay_end
  and rsv.checkout_date_time > :stay_start;
"""
