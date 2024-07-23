package ru.otus.hw.services;

import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

@SpringBootTest
class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;


    @Test
    @DisplayName("должен возвращать список авторов")
    void shouldReturnCorrectAuthorList() {
        String authorId1 = "1";
        String authorName1 = "AuthorName1";
        String authorId2 = "2";
        String authorName2 = "AuthorName2";

        Author author1 = prepareAuthor(authorId1, authorName1);
        Author author2 = prepareAuthor(authorId2, authorName2);

        AuthorDTO expectedAuthorDto1 = prepareAuthorDto(authorId1, authorName1);
        AuthorDTO expectedAuthorDto2 = prepareAuthorDto(authorId2, authorName2);

        given(authorRepository.findAll())
            .willReturn(Flux.just(author1, author2));

        Flux<AuthorDTO> foundBook = authorService.findAll();
        StepVerifier
            .create(foundBook)
            .expectNext(expectedAuthorDto1)
            .expectNext(expectedAuthorDto2)
            .expectComplete()
            .verify();
    }


    private Author prepareAuthor(String id, String authorName) {
        return new Author(id, authorName);
    }

    private AuthorDTO prepareAuthorDto(String id, String authorName) {
        return new AuthorDTO(id, authorName);
    }
}