package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDTO;

public interface GenreService {
    Flux<GenreDTO> findAll();
}
