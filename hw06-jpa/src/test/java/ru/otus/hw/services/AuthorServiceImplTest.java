package ru.otus.hw.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.AuthorDTO;

@SpringBootTest
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @Test
    @DisplayName("должен загружать список всех авторов")
    void shouldReturnCorrectAuthorList() {
        List<AuthorDTO> actualAuthors = authorService.findAll();
        List<AuthorDTO> expectedAuthors = prepareAuthors();
        assertThat(actualAuthors).containsExactlyInAnyOrderElementsOf(expectedAuthors);
    }

    private static List<AuthorDTO> prepareAuthors() {
        String authorNameTemplate = "Author_";
        return IntStream.range(1, 4)
            .mapToObj(index -> new AuthorDTO(index, authorNameTemplate + index))
            .collect(Collectors.toList());
    }
}