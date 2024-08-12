package ru.otus.hw.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.access.prepost.PostFilter;
import ru.otus.hw.dto.BookDTO;

public interface BookService {
    BookDTO findById(long id);


    List<BookDTO> findAll();

    BookDTO insert(String title, long authorId, Set<Long> genresIds);

    BookDTO update(long id, String title, long authorId, Set<Long> genresIds);

    void deleteById(long id);
}
