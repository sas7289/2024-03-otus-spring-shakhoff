package ru.otus.hw.models;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "authorProjection", types = Author.class)
public interface AuthorProjection {

    long getId();
    String getFullName();
}
