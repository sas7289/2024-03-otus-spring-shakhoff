package ru.otus.hw.dto;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseBookDTO {

    private String id;

    private String title;

    private String authorId;

    private Set<String> genreIds;
}
