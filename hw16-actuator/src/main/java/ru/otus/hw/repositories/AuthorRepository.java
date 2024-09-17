package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Author;

import java.util.Optional;
import ru.otus.hw.models.AuthorProjection;

@RepositoryRestResource(path = "authors", excerptProjection = AuthorProjection.class)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findById(long id);
}
