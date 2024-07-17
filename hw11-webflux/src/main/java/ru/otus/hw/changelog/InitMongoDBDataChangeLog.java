package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.reactivestreams.client.MongoDatabase;
import java.time.LocalDateTime;
import java.util.List;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog(order = "000")
public class InitMongoDBDataChangeLog {


    @ChangeSet(order = "001", id = "1", author = "stvort", runAlways = true)
    public void dropDb(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "002", id = "2", author = "stvort", runAlways = true)
    public void migrate(AuthorRepository authorRepository, GenreRepository genreRepository,
                        BookRepository bookRepository, CommentRepository commentRepository) {
        initAuthors(authorRepository);
        initGenres(genreRepository);
        initBooks(bookRepository);
        initComment(commentRepository);
    }

    public void initAuthors(AuthorRepository authorRepository) {
        authorRepository.save(new Author("1", "Author_1"));
        authorRepository.save(new Author("2", "Author_2"));
        authorRepository.save(new Author("3", "Author_3"));
    }

    public void initGenres(GenreRepository genreRepository) {
        genreRepository.save(new Genre("1", "Genre_1"));
        genreRepository.save(new Genre("2", "Genre_2"));
        genreRepository.save(new Genre("3", "Genre_3"));
        genreRepository.save(new Genre(null, "Genre_4"));
        genreRepository.save(new Genre(null, "Genre_5"));
        genreRepository.save(new Genre(null, "Genre_6"));
    }

    public void initBooks(BookRepository bookRepository) {
        bookRepository.save(
            new Book("1",
                "Book_1",
                new Author("1", "Author_1"),
                List.of(
                    new Genre("1", "Genre_1"),
                    new Genre("2", "Genre_2")),
                List.of(
                    new Comment("1", "content", LocalDateTime.parse("2024-05-01T13:01:15"),
                        LocalDateTime.parse("2024-05-02T13:02:15"), null))));
    }

    public void initComment(CommentRepository commentRepository) {
        commentRepository.save(new Comment("1", "content", LocalDateTime.parse("2024-05-01T13:01:15"),
            LocalDateTime.parse("2024-05-02T13:02:15"), new Book("1", "Book_1", new Author("1", "Author_1"), null, null)));
    }
}
