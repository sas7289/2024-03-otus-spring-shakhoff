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
    user_id      bigint references users (id) on delete cascade,
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

-- ACL schema sql used in HSQLDB

-- drop table acl_entry;
-- drop table acl_object_identity;
-- drop table acl_class;
-- drop table acl_sid;

create table acl_sid(
                        id bigint auto_increment primary key,
                        principal boolean not null,
                        sid varchar_ignorecase(100) not null,
                        constraint unique_uk_1 unique(sid,principal)
);

create table acl_class(
                          id bigint auto_increment primary key,
                          class varchar_ignorecase(100) not null,
                          constraint unique_uk_2 unique(class)
);

create table acl_object_identity(
                                    id bigint auto_increment primary key,
                                    object_id_class bigint not null,
                                    object_id_identity bigint not null,
                                    parent_object bigint,
                                    owner_sid bigint,
                                    entries_inheriting boolean not null,
--                                     constraint unique_uk_3 unique(object_id_class,object_id_identity),
                                    constraint foreign_fk_1 foreign key(parent_object)references acl_object_identity(id),
                                    constraint foreign_fk_2 foreign key(object_id_class)references acl_class(id),
                                    constraint foreign_fk_3 foreign key(owner_sid)references acl_sid(id)
);

create table acl_entry(
                          id bigint auto_increment primary key,
                          acl_object_identity bigint not null,
                          ace_order int not null,
                          sid bigint not null,
                          mask integer not null,
                          granting boolean not null,
                          audit_success boolean not null,
                          audit_failure boolean not null,
                          constraint unique_uk_4 unique(acl_object_identity,ace_order),
                          constraint foreign_fk_4 foreign key(acl_object_identity) references acl_object_identity(id),
                          constraint foreign_fk_5 foreign key(sid) references acl_sid(id)
);
