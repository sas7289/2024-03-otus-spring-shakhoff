package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.dto.BaseCommentDTO;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {


    public String toString(CommentDTO comment) {
        return "Id: %d, Content: %s, Created date: %s, Updated date: %s, Related book: {id: %s, title: %s}".formatted(comment.getId(), comment.getContent(), comment.getCreatedDate(), comment.getUpdatedDate(), comment.getBaseBook().getId(), comment.getBaseBook().getTitle());
    }

    public String toString(Comment comment) {
        return "Id: %d, Content: %s, Created date: %s, Updated date: %s".formatted(comment.getId(), comment.getContent(), comment.getCreatedDate(), comment.getUpdatedDate());
    }

//    public BaseCommentDTO toBaseDto(Comment comment) {
//        return new BaseCommentDTO(comment.getId(), comment.getContent(), comment.getCreatedDate(), comment.getUpdatedDate());
//    }

    public CommentDTO toDto(Comment comment) {
        AuthorDTO authorDTO = new AuthorDTO(comment.getBook().getAuthor().getId(), comment.getBook().getAuthor().getFullName());
        BaseBookDTO baseBookDTO = new BaseBookDTO(comment.getBook().getId(), comment.getBook().getTitle(), authorDTO);
        return new CommentDTO(comment.getId(), comment.getContent(), comment.getCreatedDate(), comment.getUpdatedDate(), baseBookDTO);
    }
}
