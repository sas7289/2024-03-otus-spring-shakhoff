package ru.otus.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

@JdbcTest
@Import(JdbcGenreRepository.class)
class JdbcGenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("должен загружать список всех жанров")
    void shouldReturnCorrectGenreList() {
        List<Genre> expectedGenres = prepareGenres();

        List<Genre> actualGenres = genreRepository.findAll();

        assertThat(actualGenres).containsExactlyInAnyOrderElementsOf(expectedGenres);
    }

    @Test
    @DisplayName("должен находить жанры по нескольким ID")
    void shouldReturnCorrectGenreListByIDs() {
        Set<Long> genreIDs = Set.of(1L, 3L, 5L);
        List<Genre> foundGenres = genreRepository.findAllByIds(Set.of(1L, 3L, 5L));

        List<Genre> expectedGenres = prepareGenresByIDs(genreIDs);

        assertThat(foundGenres).containsExactlyInAnyOrderElementsOf(expectedGenres);
    }

    private static List<Genre> prepareGenres() {
        String genreNameTemplate = "Genre_";
        return IntStream.range(1, 7)
            .mapToObj(index -> new Genre(index, genreNameTemplate + index))
            .collect(Collectors.toList());
    }

    private static List<Genre> prepareGenresByIDs(Set<Long> ids) {
        String genreNameTemplate = "Genre_";
        return ids.stream()
            .map(index -> new Genre(index, genreNameTemplate + index))
            .collect(Collectors.toList());
    }

}