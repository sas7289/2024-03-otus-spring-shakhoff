package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.Book;

import java.util.Optional;
import ru.otus.hw.models.BookProjection;

@RepositoryRestResource(path = "books", excerptProjection = BookProjection.class)
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findById(long id);

    void deleteById(long id);
}
