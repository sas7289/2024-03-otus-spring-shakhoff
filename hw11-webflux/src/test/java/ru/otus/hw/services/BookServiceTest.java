package ru.otus.hw.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;


    @Test
    @DisplayName("должен возвращать книгу по её ID")
    void shouldReturnCorrectBookById() {
        String bookTitle = "Title";
        Book book = prepareBook(bookTitle);
        Author author = prepareAuthor();
        Genre genre = prepareGenre();
        BookDTO expectedBookDto = prepareBookDto(bookTitle);

        given(bookRepository.findById("1"))
            .willReturn(Mono.just(book));
        given(authorRepository.findById("1"))
            .willReturn(Mono.just(author));
        given(genreRepository.findAllByIdIn(Set.of("1")))
            .willReturn(Flux.just(genre));

        Mono<BookDTO> foundBook = bookService.findById("1");
        StepVerifier
            .create(foundBook)
            .expectNext(expectedBookDto)
            .expectComplete()
            .verify();
    }

    @Test
    @DisplayName("должен возвращать список книг")
    void shouldReturnCorrectBookList() {
        String bookTitle = "Title";
        Book book = prepareBook(bookTitle);
        Author author = prepareAuthor();
        Genre genre = prepareGenre();
        BookDTO expectedBookDto = prepareBookDto(bookTitle);

        given(bookRepository.findAll())
            .willReturn(Flux.just(book));
        given(authorRepository.findById("1"))
            .willReturn(Mono.just(author));
        given(genreRepository.findAllByIdIn(Set.of("1")))
            .willReturn(Flux.just(genre));

        Flux<BookDTO> foundBook = bookService.findAll();
        StepVerifier
            .create(foundBook)
            .expectNext(expectedBookDto)
            .expectComplete()
            .verify();
    }

    @Test
    @DisplayName("должен сохранять новую книгу")
    void shouldSaveNewBook() {
        String bookTitle = "Title";
        Book book = prepareBook(bookTitle);
        BaseBookDTO expectedBaseBookDto = prepareBaseBookDto(bookTitle);
        given(bookRepository.save(any()))
            .willReturn(Mono.just(book));

        Mono<BaseBookDTO> savedBook = bookService.insert(bookTitle, "1", Set.of("1"));
        StepVerifier
            .create(savedBook)
            .expectNext(expectedBaseBookDto)
            .expectComplete()
            .verify();
    }

    @Test
    @DisplayName("должен обновлять книгу по её ID")
    void shouldUpdateBookById() {
        String bookTitle = "Title";
        Book book = prepareBook(bookTitle);
        BaseBookDTO expectedBaseBookDto = prepareBaseBookDto(bookTitle);
        given(bookRepository.save(any()))
            .willReturn(Mono.just(book));

        Mono<BaseBookDTO> savedBook = bookService.update("1", bookTitle, "1", Set.of("1"));
        StepVerifier
            .create(savedBook)
            .expectNext(expectedBaseBookDto)
            .expectComplete()
            .verify();
    }

    @Test
    @DisplayName("должен удалять книгу по её ID")
    void shouldDeleteBoById() {
        String bookId = "1";
        given(bookRepository.deleteById(bookId))
            .willReturn(Mono.empty());

        bookService.deleteById(bookId);
        ArgumentCaptor<String> bookIdCapture = ArgumentCaptor.forClass(String.class);
        verify(bookRepository, times(1)).deleteById(bookIdCapture.capture());
        assertThat(bookIdCapture.getValue()).isEqualTo(bookId);
    }

    private Book prepareBook(String tittle) {
        return new Book("1", tittle, "1", Set.of("1"));
    }


    private Genre prepareGenre() {
        return new Genre("1", "GenreName");
    }

    private Author prepareAuthor() {
        return new Author("1", "AuthorName");
    }

    private GenreDTO prepareGenreDto() {
        return new GenreDTO("1", "GenreName");
    }

    private AuthorDTO prepareAuthorDto() {
        return new AuthorDTO("1", "AuthorName");
    }

    private BookDTO prepareBookDto(String tittle) {
        GenreDTO genreDTO = prepareGenreDto();
        AuthorDTO authorDTO = prepareAuthorDto();
        return new BookDTO("1", tittle, authorDTO, List.of(genreDTO));
    }

    private BaseBookDTO prepareBaseBookDto(String tittle) {
        return new BaseBookDTO("1", tittle, "1", Set.of("1"));
    }
}