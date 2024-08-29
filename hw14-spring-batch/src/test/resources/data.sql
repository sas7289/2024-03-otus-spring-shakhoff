insert into authors(full_name)
values ('Author_1'), ('Author_2');

insert into genres(name)
values ('Genre_1'), ('Genre_2');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2);

insert into books_genres(book_id, genre_id)
values (1, 1),   (2, 2);