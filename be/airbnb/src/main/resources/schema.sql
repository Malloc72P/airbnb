drop table if exists `user`;
drop table if exists `host`;
drop table if exists `guest`;
drop table if exists location;
drop table if exists room;
drop table if exists reservation;
drop table if exists room_price;
drop table if exists room_image;

create table `user`
(
    id               int primary key auto_increment,
    oauth_id         varchar(100),
    authenticated_by varchar(20),
    nickname         varchar(30),
    name             varchar(30),
    profile_image    varchar(300),
    access_token     varchar(300)
);
create table `host`
(
    user_id      int references `user` (id),
    is_superhost bool
);

create table `guest`
(
    user_id int references `user` (id)
);

create table `location`
(
    id        int primary key auto_increment,
    name      varchar(100),
    type      varchar(30),
    parent_id int references location (id),
    level     int,
    place_id  varchar(60),
    point     point
);

create table `room`
(
    id             int primary key auto_increment,
    location_id    int references location (id),
    host_id        int references host (user_id),
    name           varchar(60),
    rating         float,
    guest_capacity int,
    point          point,
    description    varchar(300),
    bedroom_type   varchar(20),
    bed_count      int,
    bathroom_type  varchar(20),
    amenity        varchar(50),
    review_count   int,
    thumbnail      varchar(300)
);

create table `room_price`
(
    id               int primary key auto_increment,
    room_id          int references room (id),
    service_fee      int,
    accomodation_tax int,
    clean_up_cost    int,
    price_per_day    int,
    weekly_discount  int
);

create table `room_image`
(
    room_id     int references room (id),
    image_url   varchar(300),
    image_index int,
    primary key (room_id, image_index)
);

create table `reservation`
(
    id                 int primary key auto_increment,
    guest_id           int references guest (user_id),
    room_id            int references room (id),
    checkin_date  date,
    checkout_date date,
    adult_count        int,
    child_count        int,
    infant_count       int
);
