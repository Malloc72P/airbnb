set foreign_key_checks = 0;
drop table if exists `user`;
drop table if exists `host`;
drop table if exists `guest`;
drop table if exists location;
drop table if exists room;
drop table if exists reservation;
drop table if exists room_image;
set foreign_key_checks = 1;

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
    user_id      int not null,
    is_superhost bool,
    primary key (user_id),
    foreign key (user_id) references `user` (id)
);

create table `guest`
(
    user_id int not null,
    primary key (user_id),
    foreign key (user_id) references `user` (id)
);

create table `location`
(
    id        int primary key auto_increment,
    name      varchar(100),
    type      varchar(30),
    parent_id int,
    level     int,
    place_id  varchar(60),
    point     point,
    foreign key (parent_id) references location (id)
);

create table `room`
(
    id             int primary key auto_increment,
    location_id    int not null,
    host_id        int not null,
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
    thumbnail      varchar(300),
    service_fee      int,
    accomodation_tax int,
    clean_up_cost    int,
    price_per_day    int,
    weekly_discount  int,
    foreign key (location_id) references location (id),
    foreign key (host_id) references host (user_id)
);

create table `room_image`
(
    room_id     int not null,
    image_url   varchar(300),
    image_index int,
    primary key (room_id, image_index),
    foreign key (room_id) references room (id)
);

create table `reservation`
(
    id            int primary key auto_increment,
    guest_id      int not null,
    room_id       int not null,
    checkin_date  date,
    checkout_date date,
    adult_count   int,
    child_count   int,
    infant_count  int,
    foreign key (guest_id) references guest (user_id),
    foreign key (room_id) references room (id)
);
