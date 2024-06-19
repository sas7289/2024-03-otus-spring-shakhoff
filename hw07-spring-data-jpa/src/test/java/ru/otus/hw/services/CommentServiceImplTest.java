package ru.otus.hw.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.dto.CommentDTO;

@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @DisplayName("должен загружать список комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentList() {
        long bookId = 1;
        List<CommentDTO> actualComments = commentService.findByBookId(bookId);
        List<CommentDTO> expectedComments = getDbComments();
        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
    }

    private static List<CommentDTO> getDbComments() {
        BaseBookDTO dbBook = getDbBook();
        return IntStream.range(1, 3).boxed()
            .map(id -> new CommentDTO(id,
                "Content_" + id,
                LocalDateTime.parse(String.format("2024-05-0%sT13:0%s:15", id, id)),
                LocalDateTime.parse(String.format("2024-06-0%sT13:0%s:15", id, id)),
                dbBook
            ))
            .toList();
    }

    private static BaseBookDTO getDbBook() {
        return new BaseBookDTO(1, "BookTitle_1", new AuthorDTO(1, "Author_1"));
    }
}