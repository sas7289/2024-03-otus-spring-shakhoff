package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {


    public String commentDtotoString(CommentDTO comment) {
        return "Id: %s, Content: %s, Created date: %s, Updated date: %s, Related book: {id: %s, title: %s}"
            .formatted(comment.getId(), comment.getContent(), comment.getCreatedDate(),
                comment.getUpdatedDate(), comment.getBaseBook().getId(), comment.getBaseBook().getTitle());
    }

    public CommentDTO toDto(Comment comment) {
        AuthorDTO authorDTO = new AuthorDTO(comment.getBook().getAuthor().getId(),
            comment.getBook().getAuthor().getFullName());
        BaseBookDTO baseBookDTO = new BaseBookDTO(comment.getBook().getId(), comment.getBook().getTitle(), authorDTO);
        return new CommentDTO(comment.getId(), comment.getContent(), comment.getCreatedDate(), comment.getUpdatedDate(),
            baseBookDTO);
    }
}
