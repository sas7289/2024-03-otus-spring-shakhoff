package ru.otus.hw.services;

import java.util.Set;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDTO;

public interface BookService {
    Mono<BookDTO> findById(String id);

    Flux<BookDTO> findAll();

    Mono<BookDTO> insert(String title, long authorId, Set<Long> genresIds);

    Mono<BookDTO> update(String id, String title, long authorId, Set<Long> genresIds);

    void deleteById(String id);
}
