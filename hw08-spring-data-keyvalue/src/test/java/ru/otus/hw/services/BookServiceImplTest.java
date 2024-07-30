package ru.otus.hw.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.shell.boot.StandardCommandsAutoConfiguration;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Book;

@SpringBootTest
@EnableAutoConfiguration(exclude = {StandardCommandsAutoConfiguration.class})
@TestMethodOrder(OrderAnnotation.class)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookConverter bookConverter;

    @DisplayName("должен загружать книгу по id")
    @Test
    @Order(1)
    void shouldReturnCorrectBookById() {
        assertThat(bookService.findById("1"))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(prepareBookDto1());
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    @Order(2)
    void shouldDeleteBook() {
        BookDTO bookDTO = bookConverter.toDto(mongoTemplate.findById("1", Book.class));
        assertThat(bookDTO)
            .usingRecursiveComparison()
            .isEqualTo(prepareBookDto1());
        bookService.deleteById("1");

        assertThat(mongoTemplate.findById("1", Book.class))
            .isNull();
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    @Order(3)
    void shouldSaveNewBook() {
        assertThat(mongoTemplate.findById("5", Book.class))
            .isNull();
        BookDTO book3 = prepareBookDto5();
        String authorId = book3.getAuthor().getId();
        List<String> genreIds = book3.getGenres().stream().map(GenreDTO::getId).toList();
        BookDTO savedBook = bookService.insert(book3.getTitle(), authorId, new HashSet<>(genreIds));

        assertThat(savedBook)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(prepareBookDto5());
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    @Order(4)
    void shouldSaveUpdatedBook() {
        String bookId = "2";
        BookDTO expectedBookDto = prepareUpdatedBookDto2();
        assertThat(bookConverter.toDto(mongoTemplate.findById(expectedBookDto.getId(), Book.class)))
            .usingRecursiveComparison()
            .isNotEqualTo(expectedBookDto);

        String expectedAuthorId = expectedBookDto.getAuthor().getId();
        Set<String> expectedGenreIds = expectedBookDto.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toSet());

        BookDTO updatedBook = bookService.update(bookId, expectedBookDto.getTitle(), expectedAuthorId, expectedGenreIds);
        assertThat(updatedBook)
            .usingRecursiveComparison()
            .isEqualTo(expectedBookDto);
    }

    private BookDTO prepareBookDto1() {
        AuthorDTO author = new AuthorDTO("1", "Author_1");

        GenreDTO genre1 = new GenreDTO("1", "Genre_1");
        GenreDTO genre2 = new GenreDTO("2", "Genre_2");

        BookDTO bookDto = new BookDTO("1", "Book_1", author, List.of(genre1, genre2), null);
        BaseBookDTO bookBaseDto = new BaseBookDTO("1", "Book_1", author);

        LocalDateTime originDate = LocalDateTime.parse("2024-05-01T13:01:15");
        LocalDateTime updateDate = LocalDateTime.parse("2024-06-02T13:02:15");
        CommentDTO comment = new CommentDTO("1", "Content_1", originDate, updateDate, bookBaseDto);

        bookDto.setComments(List.of(comment));

        return bookDto;
    }

    private BookDTO prepareUpdatedBookDto2() {
        AuthorDTO author = new AuthorDTO("1", "Author_1");

        GenreDTO genre1 = new GenreDTO("1", "Genre_1");
        GenreDTO genre2 = new GenreDTO("2", "Genre_2");

        BookDTO bookDto = new BookDTO("2", "UpdatedTitle", author, List.of(genre1, genre2), null);
        BaseBookDTO bookBaseDto = new BaseBookDTO("2", "UpdatedTitle", author);

        LocalDateTime originDate = LocalDateTime.parse("2024-05-01T13:01:15");
        LocalDateTime updateDate = LocalDateTime.parse("2024-06-02T13:02:15");
        CommentDTO comment = new CommentDTO("2", "Content_2", originDate, updateDate, bookBaseDto);

        bookDto.setComments(List.of(comment));

        return bookDto;
    }

    private BookDTO prepareBookDto5() {
        AuthorDTO author = new AuthorDTO("3", "Author_3");

        GenreDTO genre1 = new GenreDTO("5", "Genre_5");
        GenreDTO genre2 = new GenreDTO("6", "Genre_6");

        return new BookDTO("5", "Book_Dto", author, List.of(genre1, genre2), Collections.emptyList());
    }
}