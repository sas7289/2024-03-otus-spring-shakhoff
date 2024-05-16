package ru.otus.hw.repositories;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private static final String ID = "id";

    private static final String TITLE = "title";

    private static final String AUTHOR_ID = "author_id";

    private static final String BOOK_ID = "book_id";

    private static final String GENRE_ID = "genre_id";

    private static final String BOOK_TITLE = "book_title";

    private static final String AUTHOR_NAME = "author_name";

    private static final String GENRE_NAME = "genre_name";

    private final GenreRepository genreRepository;

    private final JdbcOperations jdbcOperations;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Long> params = Map.of(ID, id);
        return Optional.ofNullable(namedParameterJdbcTemplate.query(
            "select "
                + " b.id as book_id, b.title as book_title,"
                + " g.id as genre_id, g.name as genre_name,"
                + " a.id as author_id, a.full_name as author_name"
                + " from books as b join books_genres on b.id=books_genres.book_id "
                + "join genres as g on books_genres.genre_id =g.id "
                + "join authors as a on b.author_id = a.id    where b.id  = :id",
            params, new BookResultSetExtractor()));
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Long> params = Map.of(ID, id);
        namedParameterJdbcTemplate.update("delete from books where id = :id", params);
    }

    private List<Book> getAllBooksWithoutGenres() {
        return namedParameterJdbcTemplate.query(
            "select books.id, books.title, authors.id, authors.full_name "
                + "from books join authors ON books.author_id=authors.id",
            new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbcOperations.query("select book_id, genre_id from books_genres",
            (rs, rowNum) -> new BookGenreRelation(rs.getLong(BOOK_ID), rs.getLong(GENRE_ID)));
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Book> books = booksWithoutGenres.stream()
            .collect(Collectors.toMap(Book::getId, book -> book));
        Map<Long, Genre> genr = genres.stream()
            .collect(Collectors.toMap(Genre::getId, genre -> genre));

        relations.forEach(rel -> {
            Book book = books.get(rel.bookId);
            Genre genre = genr.get(rel.genreId);

            if (CollectionUtils.isEmpty(book.getGenres())) {
                List<Genre> g = new ArrayList<>();
                g.add(genre);
                book.setGenres(g);
            } else if (!book.getGenres().contains(genre)) {
                book.getGenres().add(genre);
            }
        });
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
            .addValue(TITLE, book.getTitle())
            .addValue(AUTHOR_ID, book.getAuthor().getId());
        namedParameterJdbcTemplate.update("insert into books (title, author_id) values (:title, :author_id)",
            params, keyHolder);

        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        Book existBook = findById(book.getId())
            .orElseThrow();

        Map<String, Object> params = Map.of(
            ID, book.getId(),
            TITLE, book.getTitle(),
            AUTHOR_ID, book.getAuthor().getId());
        int updated = namedParameterJdbcTemplate.update("update books set title = :title, author_id = :author_id "
            + "where id = :id", params);
        if (updated == 0) {
            throw new EntityNotFoundException("No records were updated");
        }
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        removeGenresRelationsFor(existBook);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        SqlParameterSource[] params = book.getGenres().stream()
            .map(genre -> new MapSqlParameterSource()
                .addValue(BOOK_ID, book.getId())
                .addValue(GENRE_ID, genre.getId()))
            .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate("insert into books_genres (book_id, genre_id)"
            + " values (:book_id, :genre_id)", params);
        // Использовать метод batchUpdate
    }

    private void removeGenresRelationsFor(Book book) {
        //...
        SqlParameterSource[] params = book.getGenres().stream()
            .map(genre -> new MapSqlParameterSource()
                .addValue(BOOK_ID, book.getId())
                .addValue(GENRE_ID, genre.getId()))
            .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate("delete books_genres where book_id = :book_id "
            + "AND genre_id = :genre_id)", params);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(rs.getLong("books.id"), rs.getString("books.title"),
                new Author(rs.getLong("authors.id"), rs.getString("full_name")), null);
        }
    }

    // Использовать для findById
    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Genre> genres = new ArrayList<>();
            long bookId = 0;
            String bookTitle = "";
            long authorId = 0;
            String authorName = "";
            while (rs.next()) {
                bookId = rs.getLong(BOOK_ID);
                bookTitle = rs.getString(BOOK_TITLE);
                authorId = rs.getLong(AUTHOR_ID);
                authorName = rs.getString(AUTHOR_NAME);
                long genreId = rs.getLong(GENRE_ID);
                String genreName = rs.getString(GENRE_NAME);
                genres.add(new Genre(genreId, genreName));
            }
            Author author = new Author(authorId, authorName);
            return new Book(bookId, bookTitle, author, genres);
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {

    }
}
