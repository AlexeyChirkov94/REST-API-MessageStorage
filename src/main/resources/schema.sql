drop table if exists USERS CASCADE;
drop table if exists TOKENS CASCADE;
drop table if exists MESSAGES CASCADE;

create table USERS
(
    id serial primary key,
    username varchar(50) unique not null,
    password varchar(60) not null
);

create table TOKENS
(
    id serial primary key,
    value varchar (125) unique not null,
    date_of_expire timestamp,
    user_id bigint references USERS(id)
);

create table MESSAGES
(
    id serial primary key,
    datetime_of_message timestamp not null,
    value text not null,
    author_id  bigint references USERS(id)
);