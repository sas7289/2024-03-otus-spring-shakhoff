package ru.otus.hw.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.services.BookService;

import java.util.Set;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public List<BookDTO> findAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/books/{id}")
    public BookDTO findBookById(@PathVariable("id") long id) {
        return bookService.findById(id)
            .orElseThrow(() -> new RuntimeException("ALARM!"));
    }

    @PostMapping("/books")
    public BookDTO insertBook(@RequestParam String title, @RequestParam long authorId,
                              @RequestParam Set<Long> genresIds) {
        return bookService.insert(title, authorId, genresIds);
    }

    @PutMapping("/books/update")
    public BookDTO updateBook(@RequestParam long id,
                              @RequestParam String title,
                              @RequestParam long authorId,
                              @RequestParam Set<Long> genresIds) {
        return bookService.update(id, title, authorId, genresIds);
    }

    @DeleteMapping("books/delete/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
