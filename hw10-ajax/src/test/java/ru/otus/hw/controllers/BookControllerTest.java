package ru.otus.hw.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("должен возвращать список книг")
    void shouldReturnCorrectBookList() throws Exception {
        AuthorDTO authorDTO = prepareAuthor();
        GenreDTO genreDTO = prepareGenre();
        BookDTO bookDTO = prepareBook(authorDTO, genreDTO);

        given(bookService.findAll())
            .willReturn(List.of(bookDTO));
        given(authorService.findAll())
            .willReturn(List.of(authorDTO));
        given(genreService.findAll())
            .willReturn(List.of(genreDTO));

        this.mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("BookTitle"))
            .andExpect(jsonPath("$[0].author.id").value("1"))
            .andExpect(jsonPath("$[0].author.fullName").value("AuthorName"))
            .andExpect(jsonPath("$[0].genres[0].id").value("1"))
            .andExpect(jsonPath("$[0].genres[0].name").value("GenreName"));
    }

    @Test
    @DisplayName("должен возвращать книгу по её ID")
    void shouldReturnCorrectBookById() throws Exception {
        AuthorDTO authorDTO = prepareAuthor();
        GenreDTO genreDTO = prepareGenre();
        BookDTO bookDTO = prepareBook(authorDTO, genreDTO);

        given(bookService.findById(1))
            .willReturn(Optional.of(bookDTO));
        given(authorService.findAll())
            .willReturn(List.of(authorDTO));
        given(genreService.findAll())
            .willReturn(List.of(genreDTO));

        this.mockMvc.perform(get("/books/{bookId}", "1"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("BookTitle"))
            .andExpect(jsonPath("$.author.id").value("1"))
            .andExpect(jsonPath("$.author.fullName").value("AuthorName"))
            .andExpect(jsonPath("$.genres[0].id").value("1"))
            .andExpect(jsonPath("$.genres[0].name").value("GenreName"));
    }

    @Test
    @DisplayName("должен сохранять новую книгу")
    void shouldSaveNewBook() throws Exception {
        AuthorDTO authorDTO = prepareAuthor();
        GenreDTO genreDTO = prepareGenre();
        BookDTO bookDTO = prepareBook(authorDTO, genreDTO);

        String newBookTitle = "new book title";

        ArgumentCaptor<String> titleCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> authorIdCapture = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Set> genresIdCapture = ArgumentCaptor.forClass(Set.class);

        given(bookService.insert(newBookTitle, 3, Set.of(3L, 4L)))
            .willReturn(bookDTO);

        this.mockMvc.perform(post("/books")
                .queryParam("title", newBookTitle)
                .queryParam("authorId", String.valueOf(3))
                .queryParam("genresIds", String.valueOf(3), String.valueOf(4)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("BookTitle"))
            .andExpect(jsonPath("$.author.id").value("1"))
            .andExpect(jsonPath("$.author.fullName").value("AuthorName"))
            .andExpect(jsonPath("$.genres[0].id").value("1"))
            .andExpect(jsonPath("$.genres[0].name").value("GenreName"));
        verify(bookService, times(1)).insert(titleCapture.capture(), authorIdCapture.capture(), genresIdCapture.capture());
        assertThat(titleCapture.getValue()).isEqualTo(newBookTitle);
        assertThat(authorIdCapture.getValue()).isEqualTo(3);

        Set<Long> longs = Set.of(3L, 4L);
        assertThat(genresIdCapture.getValue()).isEqualTo(longs);
    }

    @Test
    @DisplayName("должен обновлять книгу по её ID")
    void shouldUpdateBookById() throws Exception {
        AuthorDTO authorDTO = prepareAuthor();
        GenreDTO genreDTO = prepareGenre();
        BookDTO bookDTO = prepareBook(authorDTO, genreDTO);
        given(bookService.findById(1))
            .willReturn(Optional.of(bookDTO));
        given(authorService.findAll())
            .willReturn(List.of(authorDTO));
        given(genreService.findAll())
            .willReturn(List.of(genreDTO));

        long bookId = 1;
        String updatedBookTitle = "updated book title";

        ArgumentCaptor<Long> bookIdCapture = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> titleCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> authorIdCapture = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Set> genresIdCapture = ArgumentCaptor.forClass(Set.class);

        given(bookService.update(bookId, updatedBookTitle, 3, Set.of(3L, 4L)))
            .willReturn(bookDTO);

        this.mockMvc.perform(
                put("/books/{id}", bookId)
                    .queryParam("title", updatedBookTitle)
                    .queryParam("authorId", String.valueOf(3))
                    .queryParam("genresIds", String.valueOf(3), String.valueOf(4)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("BookTitle"))
            .andExpect(jsonPath("$.author.id").value("1"))
            .andExpect(jsonPath("$.author.fullName").value("AuthorName"))
            .andExpect(jsonPath("$.genres[0].id").value("1"))
            .andExpect(jsonPath("$.genres[0].name").value("GenreName"));
        verify(bookService, times(1))
            .update(bookIdCapture.capture(), titleCapture.capture(), authorIdCapture.capture(), genresIdCapture.capture());
        assertThat(bookIdCapture.getValue()).isEqualTo(1);
        assertThat(titleCapture.getValue()).isEqualTo(updatedBookTitle);
        assertThat(authorIdCapture.getValue()).isEqualTo(3);

        Set<Long> longs = Set.of(3L, 4L);
        assertThat(genresIdCapture.getValue()).isEqualTo(longs);
    }

    @Test
    @DisplayName("должен удалять книгу по её ID")
    void shouldDeleteBoById() throws Exception {
        AuthorDTO authorDTO = prepareAuthor();
        GenreDTO genreDTO = prepareGenre();
        prepareBook(authorDTO, genreDTO);

        ArgumentCaptor<Long> bookIdCapture = ArgumentCaptor.forClass(Long.class);

        this.mockMvc.perform(delete("/books/{bookId}", "1"))
            .andExpect(status().isOk());
        verify(bookService, times(1)).deleteById(bookIdCapture.capture());
        assertThat(bookIdCapture.getValue()).isEqualTo(1);
    }

    @Test
    @DisplayName("Должен выбрасывать исключение EntityNotFoundException при поиске книги по Id")
    void shouldThrowEntityNotFoundExceptionWhenFindBookById() throws Exception {
        int bookId = 1;
        String expectedExceptionMessage = String.format("Book not found by id: %s", bookId);

        given(bookService.findById(bookId))
            .willReturn(Optional.empty());

        this.mockMvc.perform(get("/books/{bookId}", "1"))
            .andExpect(result -> Assertions.assertInstanceOf(RuntimeException.class, result.getResolvedException()))
            .andExpect(result -> Assertions.assertEquals(expectedExceptionMessage, result.getResolvedException().getMessage()));
    }

    private BookDTO prepareBook(AuthorDTO authorDTO, GenreDTO genreDTO) {
        return new BookDTO(1, "BookTitle", authorDTO, List.of(genreDTO), new ArrayList<>());
    }

    private GenreDTO prepareGenre() {
        return new GenreDTO(1, "GenreName");
    }

    private AuthorDTO prepareAuthor() {
        return new AuthorDTO(1, "AuthorName");
    }
}