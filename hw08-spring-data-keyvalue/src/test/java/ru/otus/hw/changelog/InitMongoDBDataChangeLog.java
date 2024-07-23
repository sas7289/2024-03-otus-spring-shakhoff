package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
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


    @ChangeSet(order = "001", id = "dropDB", author = "sharkVale", runAlways = true)
    public void dropDb(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "002", id = "initData", author = "sharkVale", runAlways = true)
    public void migrate(AuthorRepository authorRepository, GenreRepository genreRepository, BookRepository bookRepository, CommentRepository commentRepository) {
        Author author1 = new Author("1", "Author_1");
        Author author2 = new Author("2", "Author_2");
        Author author3 = new Author("3", "Author_3");
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);

        Genre genre1 = new Genre("1", "Genre_1");
        Genre genre2 = new Genre("2", "Genre_2");
        Genre genre3 = new Genre("3", "Genre_3");
        Genre genre4 = new Genre("4", "Genre_4");
        Genre genre5 = new Genre("5", "Genre_5");
        Genre genre6 = new Genre("6", "Genre_6");
        genreRepository.save(genre1);
        genreRepository.save(genre2);
        genreRepository.save(genre3);
        genreRepository.save(genre4);
        genreRepository.save(genre5);
        genreRepository.save(genre6);

        Book book1 = new Book("1", "Book_1", author1, List.of(genre1, genre2), null);
        Book book2 = new Book("2", "Book_2", author2, List.of(genre3, genre4), null);
        Book book3 = new Book("3", "Book_3", author3, List.of(genre4, genre5), null);
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        LocalDateTime originDate = LocalDateTime.parse("2024-05-01T13:01:15");
        LocalDateTime updateDate = LocalDateTime.parse("2024-06-02T13:02:15");
        Comment comment1 = new Comment("1", "Content_1", originDate, updateDate, book1);
        Comment comment2 = new Comment("2", "Content_2", originDate, updateDate, book2);
        Comment comment3 = new Comment("3", "Content_3", originDate, updateDate, book3);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        book1.setComments(List.of(comment1));
        book2.setComments(List.of(comment2));
        book3.setComments(List.of(comment3));
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }
}
