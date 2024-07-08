package ru.otus.hw.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;

import java.util.Set;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books")
    public String findAllBooks(Model model) {
        List<BookDTO> books = bookService.findAll();
        List<AuthorDTO> authors = authorService.findAll();
        List<GenreDTO> genres = genreService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book-list";
    }

    @GetMapping("/books/{id}")
    public String findBookById(@PathVariable("id") long id, Model model) {
        BookDTO book = bookService.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Book not found by id: " + id));
        model.addAttribute("book", book);
        return "book";
    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable("id") long id, Model model) {
        BookDTO book = bookService.findById(id)
            .orElseThrow(() -> new RuntimeException("ALARM!"));
        List<AuthorDTO> authors = authorService.findAll();
        List<GenreDTO> genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "edit";
    }

    @PostMapping("/books")
    public String insertBook(@RequestParam String title, @RequestParam long authorId,
                             @RequestParam Set<Long> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return "redirect:/books";
    }

    @PostMapping("/books/update")
    public String updateBook(@RequestParam long id,
                             @RequestParam String title,
                             @RequestParam long authorId,
                             @RequestParam Set<Long> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return "redirect:/books";
    }

    @GetMapping("books/delete/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
