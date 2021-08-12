create table client(
    id serial primary key,
    name varchar(100) not null,
    login varchar(100) not null,
    password varchar(6) not null
)