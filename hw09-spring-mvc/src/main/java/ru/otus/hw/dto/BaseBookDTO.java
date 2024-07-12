package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseBookDTO {

    private long id;

    private String title;

    private AuthorDTO author;
}
