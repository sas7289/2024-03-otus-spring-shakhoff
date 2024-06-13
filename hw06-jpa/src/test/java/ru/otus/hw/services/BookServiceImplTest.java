package ru.otus.hw.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.dto.GenreDTO;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
@Transactional(propagation = Propagation.NEVER)
@TestMethodOrder(OrderAnnotation.class)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("должен загружать список всех книг")
    @Test
    @Order(1)
    void shouldReturnCorrectBooksList() {
        List<BookDTO> actualBooks = bookService.findAll();
        var expectedBooks = getDbBooks();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    @Order(2)
    void shouldReturnCorrectBookById(BookDTO expectedBook) {
        var actualBook = bookService.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
            .get()
            .isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    @Order(3)
    void shouldDeleteBook() {
        long bookId = 1;
        assertThat(bookService.findById(bookId)).isPresent();
        bookService.deleteById(1);

        assertThat(bookService.findById(1)).isEmpty();
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    @Order(4)
    void shouldSaveNewBook() {
        var newBookTitle = "New book title";

        var expectedAuthor = new AuthorDTO(1, "Author_1");
        List<GenreDTO> expectedGenres = List.of(new GenreDTO(1, "Genre_1"));
        var expectedBook = new BookDTO(4, newBookTitle, expectedAuthor, expectedGenres, new ArrayList<>());

        var returnedBook = bookService.insert(newBookTitle, expectedAuthor.getId(), Set.of(expectedGenres.get(0).getId()));
        assertThat(returnedBook).isNotNull()
            .matches(book -> book.getId() > 0)
            .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    @Order(5)
    void shouldSaveUpdatedBook() {
        var updatedBookTitle = "Updated book title";

        var updatedBookAuthor = new AuthorDTO(3, "Author_3");
        List<GenreDTO> updatedBookGenres = List.of(new GenreDTO(6, "Genre_6"));
        long bookId = 2;
        var expectedBook = new BookDTO(bookId, updatedBookTitle, updatedBookAuthor, updatedBookGenres, new ArrayList<>());


        assertThat(bookService.findById(bookId))
            .isPresent()
            .get()
            .isNotEqualTo(expectedBook);

        BookDTO updatedBook = bookService.update(bookId, updatedBookTitle, updatedBookAuthor.getId(), Set.of(updatedBookGenres.get(0).getId()));

        assertThat(updatedBook).isNotNull()
            .matches(book -> book.getId() > 0)
            .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
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