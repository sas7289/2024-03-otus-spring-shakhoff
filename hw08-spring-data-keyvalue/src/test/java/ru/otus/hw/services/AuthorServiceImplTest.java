package ru.otus.hw.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.boot.StandardCommandsAutoConfiguration;
import ru.otus.hw.dto.AuthorDTO;

@SpringBootTest
@EnableAutoConfiguration(exclude = {StandardCommandsAutoConfiguration.class})
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;


    @Test
    @DisplayName("должен загружать список всех авторов")
    void shouldReturnCorrectAuthorList() {
        assertThat(authorService.findAll())
            .usingRecursiveComparison()
            .isEqualTo(prepareAuthorDtos());
    }

    private static List<AuthorDTO> prepareAuthorDtos() {
        String authorNameTemplate = "Author_";
        return IntStream.range(1, 4)
            .mapToObj(index -> new AuthorDTO(String.valueOf(index), authorNameTemplate + index))
            .collect(Collectors.toList());
    }
}