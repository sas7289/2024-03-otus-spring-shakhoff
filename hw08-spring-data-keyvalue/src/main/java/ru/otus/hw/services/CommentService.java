package ru.otus.hw.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDTO;

public interface CommentService {

    @Transactional
    Optional<CommentDTO> findById(String id);

    @Transactional
    CommentDTO insert(String content, String bookId, LocalDateTime createdDate);

    @Transactional
    CommentDTO update(String id, String content, String bookId);

    @Transactional
    void deleteById(String id);

    @Transactional(readOnly = true)
    List<CommentDTO> findByBookId(String bookId);
}
