package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog(order = "000")
public class InitMongoDBDataChangeLog {


    @ChangeSet(order = "001", id = "dropDB", author = "sharkVale", runAlways = true)
    public void dropDb(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "002", id = "initData", author = "sharkVale", runAlways = true)
    public void migrate(AuthorRepository authorRepository, GenreRepository genreRepository, BookRepository bookRepository) {
        Author author1 = authorRepository.save(new Author(null, "Author_1"));
        authorRepository.save(new Author(null, "Author_2"));
        authorRepository.save(new Author(null, "Author_3"));

        Genre genre1 = genreRepository.save(new Genre(null, "Genre_1"));
        Genre genre2 = genreRepository.save(new Genre(null, "Genre_2"));
        genreRepository.save(new Genre(null, "Genre_3"));
        genreRepository.save(new Genre(null, "Genre_4"));
        genreRepository.save(new Genre(null, "Genre_5"));
        genreRepository.save(new Genre(null, "Genre_6"));

        bookRepository.save(new Book(null, "Title", author1, List.of(genre1, genre2), null));
    }

    public void initAuthors(AuthorRepository authorRepository) {
        authorRepository.save(new Author(null, "Author_1"));
        authorRepository.save(new Author(null, "Author_2"));
        authorRepository.save(new Author(null, "Author_3"));
    }

    public void initGenres(GenreRepository genreRepository) {
        genreRepository.save(new Genre(null, "Genre_1"));
        genreRepository.save(new Genre(null, "Genre_2"));
        genreRepository.save(new Genre(null, "Genre_3"));
        genreRepository.save(new Genre(null, "Genre_4"));
        genreRepository.save(new Genre(null, "Genre_5"));
        genreRepository.save(new Genre(null, "Genre_6"));
    }

    public void initBook() {

    }
}
