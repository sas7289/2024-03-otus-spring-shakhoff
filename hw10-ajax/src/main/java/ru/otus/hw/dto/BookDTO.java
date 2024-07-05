package ru.otus.hw.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDTO {

    private long id;

    private String title;

    private AuthorDTO author;

    private List<GenreDTO> genres;

    private List<CommentDTO> comments;
}
