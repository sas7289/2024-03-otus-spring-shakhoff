package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.dto.AuthorDTO;

@Component
public class AuthorConverter {

    public String authorDtoToString(AuthorDTO authorDto) {
        return "Id: %s, FullName: %s".formatted(authorDto.getId(), authorDto.getFullName());
    }

    public AuthorDTO toDto(Author author) {
        return new AuthorDTO(author.getId(), author.getFullName());
    }
}
