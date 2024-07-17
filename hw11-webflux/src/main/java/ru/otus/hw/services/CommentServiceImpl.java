package ru.otus.hw.services;

import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDTO;
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
    public Mono<CommentDTO> findById(String id) {
        return commentRepository.findById(id)
            .map(commentConverter::toDto);
    }

    @Override
    @Transactional
    public Mono<CommentDTO> insert(String content, String bookId, LocalDateTime createdDate) {
        return bookRepository.findById(bookId)
            .map(book -> new Comment(null, content, createdDate, createdDate, book))
            .flatMap(commentRepository::save)
            .map(commentConverter::toDto);
    }

    @Override
    @Transactional
    public Mono<CommentDTO> update(String commentId, String content, long bookId) {
        Mono<Book> bookMono = bookRepository.findById(bookId);
        Mono<Comment> commentMono = commentRepository.findById(commentId);

        return commentMono.zipWith(bookMono, (comment, book) -> {
                comment.setUpdatedDate(LocalDateTime.now(Clock.systemUTC()));
                comment.setBook(book);
                comment.setContent(content);
                return comment;
            })
            .flatMap(commentRepository::save)
            .map(commentConverter::toDto);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CommentDTO> findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId)
            .map(commentConverter::toDto);
    }
}
