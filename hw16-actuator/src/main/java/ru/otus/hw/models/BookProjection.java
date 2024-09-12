package ru.otus.hw.models;

import java.util.List;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "bookProjection", types = Book.class)
public interface BookProjection {
    long getId();
    String getTitle();
    AuthorProjection getAuthor();
    List<GenreProjection> getGenres();
}
