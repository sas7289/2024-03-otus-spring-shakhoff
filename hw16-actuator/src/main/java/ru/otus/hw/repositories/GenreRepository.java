package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import ru.otus.hw.models.GenreProjection;

@RepositoryRestResource(path = "genres", excerptProjection = GenreProjection.class)
public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findAllByIdIn(Set<Long> ids);
}
