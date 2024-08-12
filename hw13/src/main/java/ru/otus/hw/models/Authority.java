package ru.otus.hw.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "authorities")
@Getter
public class Authority {

    @Id
    private long id;

    @Column(name = "name")
    private String name;

}