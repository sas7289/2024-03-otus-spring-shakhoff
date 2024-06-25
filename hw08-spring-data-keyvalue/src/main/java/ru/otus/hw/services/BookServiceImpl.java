package ru.otus.hw.services;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
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
    public Optional<BookDTO> findById(String id) {
        return bookRepository.findById(id).map(bookConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> findAll() {
        List<Book> allBooks = bookRepository.findAll();
        return allBooks.stream().map(bookConverter::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDTO insert(String title, String authorId, Set<String> genresIds) {
        Book savedBook = save(null, title, authorId, genresIds);
        return bookConverter.toDto(savedBook);
    }

    @Override
    @Transactional
    public BookDTO update(String id, String title, String authorId, Set<String> genresIds) {
        Book updatedBook = save(id, title, authorId, genresIds);
        return bookConverter.toDto(updatedBook);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.findByBookId(id).stream()
            .map(Comment::getId)
            .forEach(commentRepository::deleteById);
        bookRepository.deleteById(id);
    }

    private Book save(String id, String title, String authorId, Set<String> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
            .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        List<Comment> comments = commentRepository.findByBookId(id);

        var book = new Book(id, title, author, genres, comments);
        return bookRepository.save(book);
    }
}
