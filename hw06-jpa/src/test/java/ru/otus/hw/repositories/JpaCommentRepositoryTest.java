package ru.otus.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@DataJpaTest
@Import({JpaCommentRepository.class,
    CommentConverter.class})
class JpaCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CommentConverter commentConverter;


    @ParameterizedTest()
    @MethodSource("getDbComments")
    void shouldReturnCorrectCommentById(CommentDTO expectedCommentDto) {
        Optional<CommentDTO> actualCommentDto = commentRepository.findById(expectedCommentDto.getId())
            .map(commentConverter::toDto);
        assertThat(actualCommentDto)
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expectedCommentDto);
    }

    @Test
    void shouldReturnCorrectCommentListByBookId() {
        long bookId = 1;
        List<CommentDTO> actualCommentDtos = commentRepository.findByBookId(bookId).stream()
            .map(commentConverter::toDto)
            .toList();

        assertThat(actualCommentDtos).hasSameElementsAs(getDbComments());
    }

    @Test
    void shouldSaveNewComment() {
        Book book = testEntityManager.find(Book.class, 1);
        Comment newComment = new Comment(0, "Content_1", LocalDateTime.parse("2024-05-01T13:01:15"), LocalDateTime.parse("2024-05-01T13:01:15"),
            book);
        Comment savedComment = commentRepository.save(newComment);
        assertThat(savedComment)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(newComment);

        assertThat(testEntityManager.find(Comment.class, 3))
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(newComment);

    }

    @Test
    void shouldSaveUpdatedComment() {
        Book book = testEntityManager.find(Book.class, 1);
        Comment expectedComment = new Comment(1, "Content_1", LocalDateTime.parse("2024-05-01T13:01:15"), LocalDateTime.parse("2024-05-01T13:01:15"),
            book);
        assertThat(testEntityManager.find(Comment.class, 1))
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isNotEqualTo(expectedComment);

        Comment savedComment = commentRepository.save(expectedComment);
        assertThat(savedComment)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedComment);

        assertThat(testEntityManager.find(Comment.class, 1))
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .isEqualTo(expectedComment);

    }


    private static List<CommentDTO> getDbComments() {
        AuthorDTO firstAuthorDto = new AuthorDTO(1, "Author_1");
        BaseBookDTO firstBaseBookDto = new BaseBookDTO(1, "BookTitle_1", firstAuthorDto);
        return IntStream.range(1, 3).boxed()
            .map(id -> new CommentDTO(id,
                "Content_" + id,
                LocalDateTime.parse(String.format("2024-05-0%sT13:0%s:15", id, id)),
                LocalDateTime.parse(String.format("2024-06-0%sT13:0%s:15", id, id)),
                firstBaseBookDto
            ))
            .toList();
    }
}