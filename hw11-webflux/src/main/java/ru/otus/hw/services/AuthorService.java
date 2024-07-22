package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDTO;

public interface AuthorService {
    Flux<AuthorDTO> findAll();
}
