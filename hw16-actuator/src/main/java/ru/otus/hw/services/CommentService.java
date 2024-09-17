package ru.otus.hw.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDTO;

public interface CommentService {

    @Transactional
    Optional<CommentDTO> findById(long id);

    @Transactional
    CommentDTO insert(String content, long bookId, LocalDateTime createdDate);

    @Transactional
    CommentDTO update(long id, String content, long bookId);

    @Transactional
    void deleteById(long id);

    @Transactional(readOnly = true)
    List<CommentDTO> findByBookId(long bookId);
}
