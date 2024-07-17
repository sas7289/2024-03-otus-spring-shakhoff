package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    private final CommentRepository commentRepository;


    @Override
    @Transactional(readOnly = true)
    public Mono<BookDTO> findById(String id) {
        return bookRepository.findById(id).map(bookConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BookDTO> findAll() {
        return bookRepository.findAll()
            .map(bookConverter::toDto);
    }

    @Override
    @Transactional
    public Mono<BookDTO> insert(String title, long authorId, Set<Long> genresIds) {
        Mono<Book> savedBook = save(null, title, authorId, genresIds);
        return savedBook.map(bookConverter::toDto);
    }

    @Override
    @Transactional
    public Mono<BookDTO> update(String id, String title, long authorId, Set<Long> genresIds) {
        Mono<Book> updatedBook = save(id, title, authorId, genresIds);
        return updatedBook.map(bookConverter::toDto);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    private Mono<Book> save(String id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var authorMono = authorRepository.findById(authorId);
        var genres = genreRepository.findAllByIdIn(genresIds).collectList();
        var comments = commentRepository.findByBookId(id).collectList();

        return Mono.zip(authorMono, genres, comments)
            .map(objects -> {
                Author author = objects.getT1();
                List<Genre> genreList = objects.getT2();
                List<Comment> commentList = objects.getT3();

                return new Book(id, title, author, genreList, commentList);
            })
            .flatMap(bookRepository::save);
    }
}
