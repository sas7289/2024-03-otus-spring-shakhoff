package ru.otus.example.springbatch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookMongo {

    @Id
    private String id;

    @Column(name = "title")
    private String title;

    //    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    @ManyToOne(fetch = FetchType.LAZY)
    @DBRef
    private AuthorMongo author;

    @DBRef
    private List<GenreMongo> genres;
}