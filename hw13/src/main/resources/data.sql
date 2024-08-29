insert into authors(full_name)
values ('Author_1'),
       ('Author_2'),
       ('Author_3');

insert into genres(name)
values ('Genre_1'),
       ('Genre_2'),
       ('Genre_3'),
       ('Genre_4'),
       ('Genre_5'),
       ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1),
       ('BookTitle_2', 2),
       ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 5),
       (3, 6);

insert into users(name, password)
values ('admin', '$2y$10$acOnSpNyjhEtWsHqpyE6ceIjy9dyoEZ5e62lLHsXCDjSflHgv64/2'),
       ('user', '$2y$10$9eATQf4sBDwQtPETOJK7Au1eaV97P/Htj3Bwvwe6hpK2xAcUyBNI.');

insert into authorities(name)
values ('ROLE_admin'),
       ('ROLE_user');

insert into users_authorities(user_id, authority_id)
values (1, 1),
       (2, 2);


INSERT INTO acl_sid ( principal, sid)
VALUES ( 1, 'admin'),
       ( 1, 'user');
--                                              (3, 0, 'ROLE_EDITOR');

INSERT INTO acl_class (class)
VALUES ( 'ru.otus.hw.dto.BookDTO');

INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES ( 1, 1, NULL, 1, 0),
       ( 1, 2, NULL, 1, 0),
       ( 1, 3, NULL, 1, 0);

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure)
VALUES ( 1, 1, 1, 1, 1, 1, 1),
       ( 1, 2, 2, 1, 1, 1, 1),
       ( 3, 2, 2, 1, 1, 1, 1);