package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;
import ru.otus.hw.dto.GenreDTO;

@Component
public class GenreConverter {

    public String genreDtoToString(GenreDTO genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }

    public GenreDTO toDto(Genre genre) {
        return new GenreDTO(genre.getId(), genre.getName());
    }
}
