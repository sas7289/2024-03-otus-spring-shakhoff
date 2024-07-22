package ru.otus.hw.services;

import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

@SpringBootTest
class GenreServiceTest {

    @Autowired
    private GenreService authorService;

    @MockBean
    private GenreRepository authorRepository;


    @Test
    @DisplayName("должен возвращать список жанров")
    void shouldReturnCorrectGenreList() {
        String authorId1 = "1";
        String authorName1 = "GenreName1";
        String authorId2 = "2";
        String authorName2 = "GenreName2";

        Genre author1 = prepareGenre(authorId1, authorName1);
        Genre author2 = prepareGenre(authorId2, authorName2);

        GenreDTO expectedAuthorDto1 = prepareGenreDto(authorId1, authorName1);
        GenreDTO expectedAuthorDto2 = prepareGenreDto(authorId2, authorName2);

        given(authorRepository.findAll())
            .willReturn(Flux.just(author1, author2));

        Flux<GenreDTO> foundBook = authorService.findAll();
        StepVerifier
            .create(foundBook)
            .expectNext(expectedAuthorDto1)
            .expectNext(expectedAuthorDto2)
            .expectComplete()
            .verify();
    }


    private Genre prepareGenre(String id, String authorName) {
        return new Genre(id, authorName);
    }

    private GenreDTO prepareGenreDto(String id, String authorName) {
        return new GenreDTO(id, authorName);
    }
}