package ru.otus.hw.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDTO;

public interface BookService {
    Optional<BookDTO> findById(String id);

    List<BookDTO> findAll();

    BookDTO insert(String title, String authorId, Set<String> genresIds);

    @Transactional
    BookDTO update(String id, String title, String authorId, Set<String> genresIds);

    void deleteById(String id);
}
