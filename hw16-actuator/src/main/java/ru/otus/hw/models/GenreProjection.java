package ru.otus.hw.models;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "genreProjection", types = Genre.class)
public interface GenreProjection {
    long getId();
    String getName();
}
