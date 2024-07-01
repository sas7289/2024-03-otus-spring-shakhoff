package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog(order = "000")
public class InitMongoDBDataChangeLog {


    @ChangeSet(order = "001", id = "dropDB", author = "sharkVale", runAlways = true)
    public void dropDb(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "002", id = "initData", author = "sharkVale", runAlways = true)
    public void migrate(AuthorRepository authorRepository, GenreRepository genreRepository) {
        initAuthors(authorRepository);
        initGenres(genreRepository);
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
}
