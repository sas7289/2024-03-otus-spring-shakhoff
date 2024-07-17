package ru.otus.hw.services;

import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentDTO;

public interface CommentService {

    @Transactional
    Mono<CommentDTO> findById(String id);

    @Transactional
    Mono<CommentDTO> insert(String content, String bookId, LocalDateTime createdDate);

    @Transactional
    Mono<CommentDTO> update(String id, String content, long bookId);

    @Transactional
    void deleteById(String id);

    @Transactional(readOnly = true)
    Flux<CommentDTO> findByBookId(String bookId);
}
