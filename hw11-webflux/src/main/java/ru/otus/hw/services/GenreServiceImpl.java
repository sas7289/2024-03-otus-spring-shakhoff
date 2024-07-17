package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreConverter genreConverter;

    @Override
    public Flux<GenreDTO> findAll() {
        return genreRepository.findAll()
            .map(genreConverter::toDto);
    }
}
