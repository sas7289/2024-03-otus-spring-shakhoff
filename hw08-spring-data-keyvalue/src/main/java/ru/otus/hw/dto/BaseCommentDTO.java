package ru.otus.hw.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseCommentDTO {

    private String id;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
