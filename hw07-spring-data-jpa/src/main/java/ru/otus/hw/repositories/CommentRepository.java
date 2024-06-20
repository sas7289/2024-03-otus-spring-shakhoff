package ru.otus.hw.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(long id);

    void deleteById(long id);

    List<Comment> findByBookId(long bookId);
}
