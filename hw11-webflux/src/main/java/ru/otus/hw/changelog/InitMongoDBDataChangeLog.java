package ru.otus.hw.changelog;

import com.mongodb.reactivestreams.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import java.util.Set;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeUnit(id = "myMigrationChangeUnitId", order = "001", author = "mongock_test", systemVersion = "1")
public class InitMongoDBDataChangeLog {



    private AuthorRepository authorRepository;
    private GenreRepository genreRepository;
    private BookRepository bookRepository;

    public InitMongoDBDataChangeLog(AuthorRepository authorRepository, GenreRepository genreRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    @Execution
    public void migrate() {
        initAuthors(authorRepository);
        initGenres(genreRepository);
        initBooks(bookRepository);
    }

    @RollbackExecution
    public void rollbackBefore(MongoDatabase database) {
        database.drop();
    }

    public void initAuthors(AuthorRepository authorRepository) {
        Flux<Author> just = Flux.just(
            new Author("1", "Author_1"),
            new Author("2", "Author_2"),
            new Author("3", "Author_3"));
            authorRepository.saveAll(just).subscribe();
    }

    public void initGenres(GenreRepository genreRepository) {
        Flux<Genre> just = Flux.just(
            new Genre("1", "Genre_1"),
        new Genre("2", "Genre_2"),
        new Genre("3", "Genre_3"),
        new Genre(null, "Genre_4"),
        new Genre(null, "Genre_5"),
        new Genre(null, "Genre_6")
        );
        genreRepository.saveAll(just).subscribe();
    }

    public void initBooks(BookRepository bookRepository) {
        bookRepository.save(new Book("1", "Book_1", "1", Set.of("1", "2"))).subscribe();
    }
}
