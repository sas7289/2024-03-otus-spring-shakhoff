package ru.otus.hw.models;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    private String title;

    private String authorId;

    private Set<String> genreIds;
}
