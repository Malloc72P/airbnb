package com.codesquad.airbnb.web.statement

const val SAVE_RESERVATION: String = """
insert into reservation(room_id, guest_id, checkin_date, checkout_date, adult_count, child_count,infant_count)
values (:room_id, :guest_id, :checkin_date, :checkout_date, :adult_count, :child_count,:infant_count);
"""

const val FIND_RESERVATION: String = """
select id, room_id, guest_id, checkin_date, checkout_date, adult_count, child_count,infant_count
from reservation
where id = :id;
"""

const val FIND_RESERVATION_BY_GUEST_ID: String = """
select rsv.id, guest_id, checkin_date, checkout_date, adult_count, child_count,infant_count,
       r.id, r.name, r.accomodation_tax, r.clean_up_cost, r.price_per_day, r.service_fee, r.weekly_discount
from reservation rsv join room  r on r.id = rsv.room_id
where guest_id = :guest_id;
"""


const val DELETE_RESERVATION: String = """
delete from reservation
where id = :id;
"""

const val IS_RESERVATIONABLE: String = """
select if(count(id) = 0, true, false) reservationable
from reservation rsv
where rsv.room_id = :room_id
  and rsv.checkin_date < :stay_end
  and rsv.checkout_date > :stay_start;
"""
