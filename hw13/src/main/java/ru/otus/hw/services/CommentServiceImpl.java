package ru.otus.hw.services;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentConverter commentConverter;

    @Override
    @Transactional(readOnly = true)
    @Secured({"admin", "user"})
    public Optional<CommentDTO> findById(long id) {
        return commentRepository.findById(id)
            .map(commentConverter::toDto);
    }

    @Override
    @Transactional
    @Secured({"admin", "user"})
    public CommentDTO insert(String content, long bookId, LocalDateTime createdDate) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book not found by id: " + bookId));
        Comment comment = new Comment(0, content, createdDate, createdDate, book);
        comment = commentRepository.save(comment);
        return commentConverter.toDto(comment);
    }

    @Override
    @Transactional
    @Secured("admin")
    public CommentDTO update(long commentId, String content, long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book not found by id: " + bookId));
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException("Comment not found by id: " + commentId));
        comment.setUpdatedDate(LocalDateTime.now(Clock.systemUTC()));
        comment.setBook(book);
        comment.setContent(content);
        return commentConverter.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    @Secured("admin")
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId).stream()
            .map(commentConverter::toDto)
            .toList();
    }
}
