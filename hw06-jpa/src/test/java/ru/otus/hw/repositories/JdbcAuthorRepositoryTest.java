package ru.otus.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

@DisplayName("Репозиторий на основе Jdbc для работы с авторами")
@JdbcTest
@Import(JdbcAuthorRepository.class)
class JdbcAuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("должен загружать список всех авторов")
    void shouldReturnCorrectAuthorList() {
        List<Author> actualAuthors = authorRepository.findAll();
        List<Author> expectedAuthors = prepareAuthors();
        assertThat(actualAuthors).containsExactlyInAnyOrderElementsOf(expectedAuthors);
    }

    @ParameterizedTest
    @MethodSource("prepareAuthors")
    @DisplayName("должен находить автора по ID")
    void shouldReturnCorrectAuthorByID(Author expectedAuthor) {
        var actualAuthorOpt = authorRepository.findById(expectedAuthor.getId());

        assertThat(actualAuthorOpt)
            .get()
            .isEqualTo(expectedAuthor);
    }


    private static List<Author> prepareAuthors() {
        String authorNameTemplate = "Author_";
        return IntStream.range(1, 4)
            .mapToObj(index -> new Author(index, authorNameTemplate + index))
            .collect(Collectors.toList());
    }
}