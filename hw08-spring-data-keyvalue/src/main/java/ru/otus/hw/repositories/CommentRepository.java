package ru.otus.hw.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Comment;

public interface CommentRepository extends MongoRepository<Comment, String>, CommentRepositoryCustom {

    List<Comment> findByBookId(String bookId);
}
