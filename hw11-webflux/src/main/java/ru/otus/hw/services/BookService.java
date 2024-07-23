package ru.otus.hw.services;

import java.util.Set;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.dto.BookDTO;

public interface BookService {
    Mono<BookDTO> findById(String id);

    Flux<BookDTO> findAll();

    Mono<BaseBookDTO> insert(String title, String authorId, Set<String> genresIds);

    Mono<BaseBookDTO> update(String id, String title, String authorId, Set<String> genresIds);

    void deleteById(String id);
}
