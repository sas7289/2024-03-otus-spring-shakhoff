package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, Long> {

    Mono<Comment> findById(String id);

    void deleteById(String id);

    Flux<Comment> findByBookId(String bookId);
}
