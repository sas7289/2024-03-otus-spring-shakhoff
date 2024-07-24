package ru.otus.hw.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.shell.boot.StandardCommandsAutoConfiguration;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@SpringBootTest
@EnableAutoConfiguration(exclude = {StandardCommandsAutoConfiguration.class})
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentConverter commentConverter;

    @Autowired
    private BookConverter bookConverter;

    @Autowired
    private MongoTemplate mongoTemplate;


    @DisplayName("должен загружать список комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentList() {
        String bookId = "3";
        assertThat(commentService.findByBookId(bookId))
            .usingRecursiveComparison()
            .isEqualTo(List.of(prepareCommentDto()));
    }


    @DisplayName("должен создавать новый комментарий")
    @Test
    void shouldInsertComment() {
        LocalDateTime createdDate = LocalDateTime.parse("2024-05-01T22:22:22");
        CommentDTO expectedNewCommentDto = new CommentDTO(null, "new Content", createdDate, null,
            new BaseBookDTO("3", "Book_3", new AuthorDTO("3", "Author_3")));
        CommentDTO existCommentDto = prepareCommentDto();

        assertThat(bookConverter.toDto(mongoTemplate.findById("3", Book.class)))
            .extracting(BookDTO::getComments)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isNotEqualTo(List.of(existCommentDto, expectedNewCommentDto));

        Query query = new Query();
        query.addCriteria(Criteria.where("book._id").is("3"));


        List<CommentDTO> commentDtosBeforeInsert = mongoTemplate.find(query, Comment.class).stream()
            .map(commentConverter::toDto)
            .toList();
        assertThat(commentDtosBeforeInsert)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isNotEqualTo(List.of(existCommentDto, expectedNewCommentDto));

        commentService.insert("new Content", "3", createdDate);

        List<CommentDTO> commentDtosAfterInsert = mongoTemplate.find(query, Comment.class).stream()
            .map(commentConverter::toDto)
            .toList();
        assertThat(commentDtosAfterInsert)
            .usingRecursiveComparison()
            .ignoringFields("id", "updatedDate")
            .isEqualTo(List.of(existCommentDto, expectedNewCommentDto));

        assertThat(bookConverter.toDto(mongoTemplate.findById("3", Book.class)))
            .extracting(BookDTO::getComments)
            .usingRecursiveComparison()
            .ignoringFields("id", "updatedDate")
            .isEqualTo(List.of(existCommentDto, expectedNewCommentDto));
    }

    @DisplayName("должен создавать новый комментарий")
    @Test
    void shouldUpdateComment() {
        LocalDateTime createdDate = LocalDateTime.parse("2024-05-01T22:22:22");
//        CommentDTO expectedNewCommentDto = new CommentDTO(null, "new Content", createdDate, null,
//            new BaseBookDTO("3", "Book_3", new AuthorDTO("3", "Author_3")));
        CommentDTO existCommentDto = prepareCommentDto();

        assertThat(bookConverter.toDto(mongoTemplate.findById("3", Book.class)))
            .extracting(BookDTO::getComments)
            .extracting(comments -> comments.stream().filter(commentDTO -> commentDTO.getId().equals("3")).findFirst())
            .extracting(Optional::get)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(existCommentDto);



        Query query = new Query();
        query.addCriteria(Criteria.where("book._id").is("3"));


        List<CommentDTO> commentDtosBeforeInsert = mongoTemplate.find(query, Comment.class).stream()
            .map(commentConverter::toDto)
            .toList();
        assertThat(commentDtosBeforeInsert)
            .filteredOn(commentDTO -> commentDTO.getId().equals("3"))
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(List.of(existCommentDto));

        String updatedContent = "updated content";
        commentService.update("3", updatedContent, "3");


        existCommentDto.setContent(updatedContent);
        assertThat(bookConverter.toDto(mongoTemplate.findById("3", Book.class)))
            .extracting(BookDTO::getComments)
            .extracting(comments -> comments.stream().filter(commentDTO -> commentDTO.getId().equals("3")).findFirst())
            .extracting(Optional::get)
            .usingRecursiveComparison()
            .ignoringFields("id", "updatedDate")
            .isEqualTo(existCommentDto);



        List<CommentDTO> commentDtosAfterInsert = mongoTemplate.find(query, Comment.class).stream()
            .map(commentConverter::toDto)
            .toList();
        assertThat(commentDtosAfterInsert)
            .filteredOn(commentDTO -> commentDTO.getId().equals("3"))
            .usingRecursiveComparison()
            .ignoringFields("id", "updatedDate")
            .isEqualTo(List.of(existCommentDto));
    }

    private CommentDTO prepareCommentDto() {
        BaseBookDTO baseBookDTO = new BaseBookDTO("3", "Book_3", new AuthorDTO("3", "Author_3"));
        LocalDateTime originDate = LocalDateTime.parse("2024-05-01T13:01:15");
        LocalDateTime updateDate = LocalDateTime.parse("2024-06-02T13:02:15");
        return new CommentDTO("3", "Content_3", originDate, updateDate, baseBookDTO);
    }
}