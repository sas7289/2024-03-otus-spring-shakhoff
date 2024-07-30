package ru.otus.hw.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
class BookControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @ParameterizedTest
    @MethodSource("validUserNameProvider")
    @DisplayName("должен возвращать списки книг, авторов и жанров для авторизованных пользователей")
    void shouldGrantAccessToGetBooksEndpointForAuthorizedUser(String userName) throws Exception {
        this.mockMvc.perform(get("/books")
                .with(user(userName).authorities(new SimpleGrantedAuthority("ROLE_" + userName))))
            .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("invalidUserNameProvider")
    @DisplayName("должен возвращать ошибку при попытке получить список книг, авторов и жанров под неавторизованным пользователем")
    void shouldDenyAccessToGetBooksEndpointForUnauthorizedUser(String userName) throws Exception {
        this.mockMvc.perform(get("/books")
                .with(user(userName).authorities(new SimpleGrantedAuthority("ROLE_" + userName))))
            .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @MethodSource("validUserNameProvider")
    @DisplayName("должен возвращать книгу по её ID для авторизованных пользователей")
    void shouldGrantAccessToGetBookByIdEndpointForAuthorizedUser(String userName) throws Exception {
        given(bookService.findById(anyLong()))
            .willReturn(Optional.of(prepareBook(prepareAuthor(), prepareGenre())));
        this.mockMvc.perform(get("/books/{bookId}", 1)
            .with(user(userName).authorities(new SimpleGrantedAuthority("ROLE_" + userName))))
            .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("invalidUserNameProvider")
    @DisplayName("должен возвращать ошибку при попытке получить книгу по её ID под неавторизованным пользователем")
    void shouldDenyAccessToGetBookByIdEndpointForUnauthorizedUser(String userName) throws Exception {
        this.mockMvc.perform(get("/books/{bookId}", "1")
                .with(user(userName).authorities(new SimpleGrantedAuthority("ROLE_" + userName))))
            .andExpect(status().isForbidden());
    }


    @ParameterizedTest
    @MethodSource("validUserNameProvider")
    @DisplayName("должен возвращать книгу по её ID и соответствующие ей жанры и автора для авторизованных пользователей")
    void shouldGrantAccessToEditBookByIdEndpointForAuthorizedUser(String userName) throws Exception {
        given(bookService.findById(anyLong()))
            .willReturn(Optional.of(prepareBook(prepareAuthor(), prepareGenre())));
        this.mockMvc.perform(get("/books/edit/{bookId}", "1")
            .with(user(userName).authorities(new SimpleGrantedAuthority("ROLE_" + userName))))
            .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("invalidUserNameProvider")
    @DisplayName("должен возвращать ошибку при попытке получить книгу по её ID и соответствующие ей жанры и автора под неавторизованным пользователем")
    void shouldDenyAccessToEditBookByIdEndpointForUnauthorizedUser(String userName) throws Exception {
        this.mockMvc.perform(get("/books/edit/{bookId}", "1")
                .with(user(userName).authorities(new SimpleGrantedAuthority("ROLE_" + userName))))
            .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @MethodSource("validUserNameProvider")
    @DisplayName("должен позволять сохранять новую книгу авторизованным пользователям")
    void shouldGrantAccessToPostNewBookEndpointForAuthorizedUser(String userName) throws Exception {
        this.mockMvc.perform(post("/books")
                .queryParam("title", "title")
                .queryParam("authorId", String.valueOf(3))
                .queryParam("genresIds", String.valueOf(3), String.valueOf(4))
            .with(user(userName).authorities(new SimpleGrantedAuthority("ROLE_" + userName))))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/books"));
    }

    @ParameterizedTest
    @MethodSource("invalidUserNameProvider")
    @DisplayName("должен возвращать ошибку при попытке сохранить новую книгу под неавторизованным пользователем")
    void shouldDenyAccessToPostNewBookEndpointForUnauthorizedUser(String userName) throws Exception {
        this.mockMvc.perform(post("/books")
                .queryParam("title", "title")
                .queryParam("authorId", String.valueOf(3))
                .queryParam("genresIds", String.valueOf(3), String.valueOf(4))
                .with(user(userName).authorities(new SimpleGrantedAuthority("ROLE_" + userName))))
            .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @MethodSource("validUserNameProvider")
    @DisplayName("должен позволять авторизованным пользователям обновлять книгу по её ID")
    void shouldGrantAccessToPostUpdatedBookEndpointForAuthorizedUser(String userName) throws Exception {
        this.mockMvc.perform(
                post("/books/update")
                    .queryParam("id", "1")
                    .queryParam("title", "title")
                    .queryParam("authorId", String.valueOf(3))
                    .queryParam("genresIds", String.valueOf(3), String.valueOf(4))
                    .with(user(userName).authorities(new SimpleGrantedAuthority("ROLE_" + userName))))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/books"));
    }

    @ParameterizedTest
    @MethodSource("invalidUserNameProvider")
    @DisplayName("должен возвращать ошибку при попытке обновить книгу по её ID под неавторизованным пользователм")
    void shouldDenyAccessToPostUpdatedBookEndpointForUnauthorizedUser(String userName) throws Exception {
        this.mockMvc.perform(
                post("/books/update")
                    .queryParam("id", "1")
                    .queryParam("title", "title")
                    .queryParam("authorId", String.valueOf(3))
                    .queryParam("genresIds", String.valueOf(3), String.valueOf(4))
                    .with(user(userName).authorities(new SimpleGrantedAuthority("ROLE_" + userName))))
            .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @MethodSource("validUserNameProvider")
    @DisplayName("должен позволять авторизованным пользователям удалять книгу по её ID")
    void shouldGrantAccessToDeleteBookEndpointForAuthorizedUser(String userName) throws Exception {
        this.mockMvc.perform(get("/books/delete/{bookId}", "1")
                .with(user(userName).authorities(new SimpleGrantedAuthority("ROLE_" + userName))))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/books"));
    }

    @ParameterizedTest
    @MethodSource("invalidUserNameProvider")
    @DisplayName("должен возвращать ошибку при попытке удалить книгу по её ID под неавторизованным пользователем")
    void shouldDenyAccessToDeleteBookEndpointForUnauthorizedUser(String userName) throws Exception {
        this.mockMvc.perform(get("/books/delete/{bookId}", "1")
                .with(user(userName).authorities(new SimpleGrantedAuthority("ROLE_" + userName))))
            .andExpect(status().isForbidden());
    }

    private static List<String> validUserNameProvider() {
        return List.of("admin", "user");
    }

    private static List<String> invalidUserNameProvider() {
        return List.of("random", "pinch");
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