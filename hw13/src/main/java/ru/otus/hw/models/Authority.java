package ru.otus.hw.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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