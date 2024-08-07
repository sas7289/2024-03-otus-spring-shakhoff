package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.services.BookService;

import java.util.Set;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public Flux<BookDTO> findAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/books/{id}")
    public Mono<BookDTO> findBookById(@PathVariable("id") String id) {
        return bookService.findById(id);
    }

    @PostMapping("/books")
    public Mono<BaseBookDTO> insertBook(@RequestParam String title, @RequestParam String authorId,
                                        @RequestParam Set<String> genresIds) {
        return bookService.insert(title, authorId, genresIds);
    }

    @PutMapping("/books/{id}")
    public Mono<BaseBookDTO> updateBook(@PathVariable("id") String id,
                              @RequestParam String title,
                              @RequestParam String authorId,
                              @RequestParam Set<String> genresIds) {
        return bookService.update(id, title, authorId, genresIds);
    }

    @DeleteMapping("books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") String id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
