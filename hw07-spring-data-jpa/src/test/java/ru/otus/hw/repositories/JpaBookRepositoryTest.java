package ru.otus.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами ")
@DataJpaTest
@Import({
    BookConverter.class,
    AuthorConverter.class,
    GenreConverter.class,
    CommentConverter.class})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class JpaBookRepositoryTest {

    @Autowired
    private BookRepository repositoryJdbc;

    @Autowired
    private BookConverter bookConverter;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(BookDTO expectedBook) {
        var actualBook = repositoryJdbc.findById(expectedBook.getId())
            .map(bookConverter::toDto);
        assertThat(actualBook).isPresent()
            .get()
            .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repositoryJdbc.findAll().stream()
            .map(bookConverter::toDto)
            .toList();
        var expectedBooks = getDbBooks();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Author author = testEntityManager.find(Author.class, 1);
        List<Genre> genres = new ArrayList<>();
        genres.add(testEntityManager.find(Genre.class, 1));
        genres.add(testEntityManager.find(Genre.class, 2));
        var expectedBook = new Book(0, "BookTitle_10500", author,
            genres, null);
        var returnedBook = repositoryJdbc.save(expectedBook);
        assertThat(returnedBook).isNotNull()
            .matches(book -> book.getId() > 0)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook);

        assertThat(testEntityManager.find(Book.class, returnedBook.getId()))
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Author author = testEntityManager.find(Author.class, 1);
        List<Genre> genres = new ArrayList<>();
        genres.add(testEntityManager.find(Genre.class, 1));
        genres.add(testEntityManager.find(Genre.class, 2));
        var expectedBook = new Book(1L, "BookTitle_10500", author,
            genres, null);

        assertThat(testEntityManager.find(Book.class, expectedBook.getId()))
            .isNotEqualTo(expectedBook);

        var returnedBook = repositoryJdbc.save(expectedBook);
        assertThat(returnedBook).isNotNull()
            .matches(book -> book.getId() > 0)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedBook);

        assertThat(testEntityManager.find(Book.class, expectedBook.getId()))
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(testEntityManager.find(Book.class, 1)).isNotNull();
        repositoryJdbc.deleteById(1L);
        testEntityManager.flush();
        assertThat(testEntityManager.find(Book.class, 1)).isNull();
    }

    private static List<AuthorDTO> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
            .map(id -> new AuthorDTO(id, "Author_" + id))
            .toList();
    }

    private static List<GenreDTO> getDbGenres() {
        return IntStream.range(1, 7).boxed()
            .map(id -> new GenreDTO(id, "Genre_" + id))
            .toList();
    }

    private static List<BookDTO> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        var firstBookDbComments = getFirstBookDbComments();
        return prepareBooks(dbAuthors, dbGenres, firstBookDbComments);
    }

    private static List<BookDTO> prepareBooks(List<AuthorDTO> dbAuthors, List<GenreDTO> dbGenres, List<CommentDTO> dbComments) {
        List<BookDTO> books = IntStream.range(1, 4).boxed()
            .map(id -> new BookDTO(id,
                "BookTitle_" + id,
                dbAuthors.get(id - 1),
                dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2),
                new ArrayList<>()
            ))
            .map(book -> {
                if (book.getId() == 1) {
                    book.setComments(dbComments);
                }
                return book;
            })
            .toList();

        return books;
    }

    private static List<CommentDTO> getFirstBookDbComments() {
        AuthorDTO firstAuthorDto = new AuthorDTO(1, "Author_1");
        BaseBookDTO firstBaseBookDto = new BaseBookDTO(1, "BookTitle_1", firstAuthorDto);
        return IntStream.range(1, 3).boxed()
            .map(id -> new CommentDTO(id,
                "Content_" + id,
                LocalDateTime.parse(String.format("2024-05-0%sT13:0%s:15", id, id)),
                LocalDateTime.parse(String.format("2024-06-0%sT13:0%s:15", id, id)),
                firstBaseBookDto
            ))
            .toList();
    }
}