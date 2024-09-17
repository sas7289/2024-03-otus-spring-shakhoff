package ru.otus.hw.actuator;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.services.BookService;

@Component
@RequiredArgsConstructor
public class BooksExistHealthIndicator implements HealthIndicator {

    private final BookService bookService;

    @Override
    public Health health() {
        List<BookDTO> allBooks = bookService.findAll();
        if (!allBooks.isEmpty()) {
            return Health.up().build();
        }
        return Health.down().build();
    }
}
