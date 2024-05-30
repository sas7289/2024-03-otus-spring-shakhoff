package ru.otus.hw.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Book;

@Data
@AllArgsConstructor
public class CommentDTO {

    private long id;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private BaseBookDTO baseBook;
}
