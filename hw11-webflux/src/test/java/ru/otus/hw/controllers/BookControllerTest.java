package ru.otus.hw.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.FirstStep;
import reactor.test.StepVerifier.Step;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.BookService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("должен возвращать список книг")
    void shouldReturnCorrectBookList() {
        AuthorDTO authorDTO = prepareAuthor();
        GenreDTO genreDTO = prepareGenre();
        BookDTO bookDTO = prepareBook(authorDTO, genreDTO);

        given(bookService.findAll())
            .willReturn(Flux.just(bookDTO));
        var webTestClientForTest = webTestClient
            .mutate()
            .responseTimeout(Duration.ofSeconds(5))
            .build();

        Flux<BookDTO> responseBody = webTestClientForTest
            .get().uri("/books")
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk()
            .returnResult(BookDTO.class)
            .getResponseBody();

        FirstStep<BookDTO> step = StepVerifier.create(responseBody);

        Step<BookDTO> stepResult = step.expectNext(bookDTO);
        stepResult.verifyComplete();
    }

    @Test
    @DisplayName("должен возвращать книгу по её ID")
    void shouldReturnCorrectBookById() {
        AuthorDTO authorDTO = prepareAuthor();
        GenreDTO genreDTO = prepareGenre();
        BookDTO bookDTO = prepareBook(authorDTO, genreDTO);

        given(bookService.findById("1"))
            .willReturn(Mono.just(bookDTO));

        var webTestClientForTest = webTestClient
            .mutate()
            .responseTimeout(Duration.ofSeconds(5))
            .build();

        Flux<BookDTO> responseBody = webTestClientForTest
            .get().uri("/books/{bookId}", "1")
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk()
            .returnResult(BookDTO.class)
            .getResponseBody();

        FirstStep<BookDTO> step = StepVerifier.create(responseBody);

        Step<BookDTO> stepResult = step.expectNext(bookDTO);
        stepResult.verifyComplete();
    }

    @Test
    @DisplayName("должен сохранять новую книгу")
    void shouldSaveNewBook() {
        String newBookTitle = "new book title";
        BaseBookDTO expectedBaseBook = prepareBaseBook(newBookTitle);


        ArgumentCaptor<String> titleCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> authorIdCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Set> genresIdCapture = ArgumentCaptor.forClass(Set.class);

        given(bookService.insert(newBookTitle, "3", Set.of("3", "4")))
            .willReturn(Mono.just(expectedBaseBook));

        var webTestClientForTest = webTestClient
            .mutate()
            .responseTimeout(Duration.ofSeconds(5))
            .build();

        Flux<BaseBookDTO> responseBody = webTestClientForTest
            .post().uri(uriBuilder -> uriBuilder
                .path("/books")
                .queryParam("title", newBookTitle)
                .queryParam("authorId", "3")
                .queryParam("genresIds", "3", "4")
                .build())
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk()
            .returnResult(BaseBookDTO.class)
            .getResponseBody();

        FirstStep<BaseBookDTO> step = StepVerifier.create(responseBody);

        Step<BaseBookDTO> stepResult = step.expectNext(expectedBaseBook);
        stepResult.verifyComplete();

        verify(bookService, times(1)).insert(titleCapture.capture(), authorIdCapture.capture(), genresIdCapture.capture());
        assertThat(titleCapture.getValue()).isEqualTo(newBookTitle);
        assertThat(authorIdCapture.getValue()).isEqualTo("3");
    }

    @Test
    @DisplayName("должен обновлять книгу по её ID")
    void shouldUpdateBookById() {
        String updatedBookTitle = "updated book title";
        BaseBookDTO expectedBaseBook = prepareBaseBook(updatedBookTitle);

        String bookId = "1";

        ArgumentCaptor<String> bookIdCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> titleCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> authorIdCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Set> genresIdCapture = ArgumentCaptor.forClass(Set.class);

        given(bookService.update(bookId, updatedBookTitle, "3", Set.of("3", "4")))
            .willReturn(Mono.just(expectedBaseBook));


        var webTestClientForTest = webTestClient
            .mutate()
            .responseTimeout(Duration.ofSeconds(5))
            .build();

        Flux<BaseBookDTO> responseBody = webTestClientForTest
            .put().uri(uriBuilder -> uriBuilder
                .path("/books/{id}")
                .queryParam("title", updatedBookTitle)
                .queryParam("authorId", "3")
                .queryParam("genresIds", "3", "4")
                .build(bookId))
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk()
            .returnResult(BaseBookDTO.class)
            .getResponseBody();

        FirstStep<BaseBookDTO> step = StepVerifier.create(responseBody);

        Step<BaseBookDTO> stepResult = step.expectNext(expectedBaseBook);
        stepResult.verifyComplete();

        verify(bookService, times(1))
            .update(bookIdCapture.capture(), titleCapture.capture(), authorIdCapture.capture(), genresIdCapture.capture());
        assertThat(bookIdCapture.getValue()).isEqualTo("1");
        assertThat(titleCapture.getValue()).isEqualTo(updatedBookTitle);
        assertThat(authorIdCapture.getValue()).isEqualTo("3");

        Set<String> longs = Set.of("3", "4");
        assertThat(genresIdCapture.getValue()).isEqualTo(longs);
    }

    @Test
    @DisplayName("должен удалять книгу по её ID")
    void shouldDeleteBoById() {
        String bookId = "1";
        ArgumentCaptor<String> bookIdCapture = ArgumentCaptor.forClass(String.class);

        var webTestClientForTest = webTestClient
            .mutate()
            .responseTimeout(Duration.ofSeconds(5))
            .build();

        webTestClientForTest
            .delete().uri("/books/{bookId}", bookId)
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk();

        verify(bookService, times(1)).deleteById(bookIdCapture.capture());
        assertThat(bookIdCapture.getValue()).isEqualTo(bookId);
    }

    private BookDTO prepareBook(AuthorDTO authorDTO, GenreDTO genreDTO) {
        return new BookDTO("1", "BookTitle", authorDTO, List.of(genreDTO));
    }

    private BaseBookDTO prepareBaseBook(String tittle) {
        return new BaseBookDTO("1", tittle, "3", Set.of("3", "4"));
    }

    private GenreDTO prepareGenre() {
        return new GenreDTO("1", "GenreName");
    }

    private AuthorDTO prepareAuthor() {
        return new AuthorDTO("1", "AuthorName");
    }
}