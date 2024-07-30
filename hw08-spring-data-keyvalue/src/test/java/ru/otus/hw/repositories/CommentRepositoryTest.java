package ru.otus.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

@DataMongoTest
@Import({BookConverter.class, CommentConverter.class, GenreConverter.class, AuthorConverter.class})
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookConverter bookConverter;

    @Autowired
    private CommentConverter commentConverter;

    @Test
    @DisplayName("должен удалить комментарий из книги")
    void shouldDeleteCommentFromBook() {
        LocalDateTime originDate = LocalDateTime.parse("2024-05-01T13:01:15");
        LocalDateTime updateDate = LocalDateTime.parse("2024-06-02T13:02:15");

        Author author = new Author("1", "Author_1");
        Genre genre1 = new Genre("1", "Genre_1");
        Genre genre2 = new Genre("2", "Genre_2");
        Book book = new Book("1", "Book_1", author, List.of(genre1, genre2), null);
        Comment comment1 = mongoTemplate.insert(new Comment(null, "Content_111", originDate, updateDate, book));
        Comment comment2 = mongoTemplate.insert(new Comment(null, "Content_222", originDate, updateDate, book));
        CommentDTO commentDto2 = commentConverter.toDto(comment2);
        book.setComments(List.of(comment1, comment2));
        mongoTemplate.save(book);

        commentRepository.removeCommentArrayElementById(comment1.getId());

        Optional<Book> byId = bookRepository.findById(book.getId());
        assertThat(byId)
            .isPresent()
            .get()
            .extracting(bookConverter::toDto)
            .extracting(BookDTO::getComments)
            .usingRecursiveComparison()
            .isEqualTo(List.of(commentDto2));
    }
}