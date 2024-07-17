package ru.otus.hw.services;

import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
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


    @Override
    @Transactional(readOnly = true)
    public Mono<BookDTO> findById(String id) {
        return bookRepository.findById(id)
            .flatMap(book -> {
                Mono<Author> author = authorRepository.findById(book.getAuthorId());
                Mono<List<Genre>> genres = genreRepository.findAllByIdIn(new HashSet<>(book.getGenreIds()))
                    .collectList();
                return Mono.zip(author, genres)
                    .map(tuple -> bookConverter.toDto(book, tuple.getT1(), tuple.getT2()));

            });

    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BookDTO> findAll() {
        return bookRepository.findAll()
            .flatMap(book -> {
                Mono<Author> author = authorRepository.findById(book.getAuthorId());
                Mono<List<Genre>> genres = genreRepository.findAllByIdIn(new HashSet<>(book.getGenreIds()))
                    .collectList();

                return Mono.zip(author, genres)
                    .map(tuple -> bookConverter.toDto(book, tuple.getT1(), tuple.getT2()));

            });
    }

    @Override
    @Transactional
    public Mono<BaseBookDTO> insert(String title, String authorId, Set<String> genresIds) {
        Mono<Book> savedBook = save(null, title, authorId, genresIds);
        return savedBook.map(bookConverter::toDto);
    }

    @Override
    @Transactional
    public Mono<BaseBookDTO> update(String id, String title, String authorId, Set<String> genresIds) {
        Mono<Book> updatedBook = save(id, title, authorId, genresIds);
        return updatedBook.map(bookConverter::toDto);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    private Mono<Book> save(String id, String title, String authorId, Set<String> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        Book book = new Book(id, title, authorId, genresIds);
        return bookRepository.save(book);
    }
}
