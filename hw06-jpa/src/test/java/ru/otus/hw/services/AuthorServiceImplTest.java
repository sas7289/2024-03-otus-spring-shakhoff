package ru.otus.hw.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.repositories.JpaAuthorRepository;

@DataJpaTest
@Import({JpaAuthorRepository.class,
    AuthorServiceImpl.class,
    AuthorConverter.class})
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