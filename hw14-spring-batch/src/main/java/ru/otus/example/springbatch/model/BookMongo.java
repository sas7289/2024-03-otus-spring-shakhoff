package ru.otus.example.springbatch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document("books")
public class BookMongo {

    @Id
    private String id;

    @Column(name = "title")
    private String title;

    @DBRef
    private AuthorMongo author;

    @DBRef
    private List<GenreMongo> genres;
}