package ru.otus.example.springbatch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "books")
//@NamedEntityGraph(name = "authors-entity-graph",
//    attributeNodes = {@NamedAttributeNode("author")})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

        @Column(name = "title")
    private String title;

    //    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "author_id")
    private String authorId;

}