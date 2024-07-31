create table authorities
(
    id   bigserial,
    name varchar(255),
    primary key (id)
);

create table users
(
    id           bigserial,
    name         varchar(255),
    password     varchar(255),
    expired_date datetime,
--     authority_id bigint references authorities (id) on delete cascade,
    primary key (id)
);

create table users_authorities
(
    user_id  bigint references users (id) on delete cascade,
    authority_id bigint references authorities (id) on delete cascade,
    primary key (user_id, authority_id)
);



create table authors
(
    id        bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres
(
    id   bigserial,
    name varchar(255),
    primary key (id)
);

create table books
(
    id        bigserial,
    title     varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table comments
(
    id           bigserial,
    content      varchar(255),
    created_date datetime,
    updated_date datetime,
    book_id      bigint references books (id) on delete cascade,
    primary key (id)
);

create table books_genres
(
    book_id  bigint references books (id) on delete cascade,
    genre_id bigint references genres (id) on delete cascade,
    primary key (book_id, genre_id)
);