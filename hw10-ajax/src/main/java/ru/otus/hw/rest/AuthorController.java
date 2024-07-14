package ru.otus.hw.rest;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/authors")
    public List<AuthorDTO> findAllBooks(Model model) {
        return authorService.findAll();
    }

//    @GetMapping("/books/{id}")
//    public BookDTO findBookById(@PathVariable("id") long id, Model model) {
//        return bookService.findById(id)
//            .orElseThrow(() -> new RuntimeException("ALARM!"));
//    }
//
//    @GetMapping("/books/edit/{id}")
//    public String editBook(@PathVariable("id") long id, Model model) {
//        BookDTO book = bookService.findById(id)
//            .orElseThrow(() -> new RuntimeException("ALARM!"));
//        List<AuthorDTO> authors = authorService.findAll();
//        List<GenreDTO> genres = genreService.findAll();
//        model.addAttribute("book", book);
//        model.addAttribute("authors", authors);
//        model.addAttribute("genres", genres);
//        return "edit";
//    }
//
//    @PostMapping("/books")
//    public String insertBook(@RequestParam String title, @RequestParam long authorId,
//                             @RequestParam Set<Long> genresIds) {
//        var savedBook = bookService.insert(title, authorId, genresIds);
//        return "redirect:/books";
//    }
//
//    @PostMapping("/books/update")
//    public String updateBook(@RequestParam long id,
//                             @RequestParam String title,
//                             @RequestParam long authorId,
//                             @RequestParam Set<Long> genresIds) {
//        var savedBook = bookService.update(id, title, authorId, genresIds);
//        return "redirect:/books";
//    }
//
//    @GetMapping("books/delete/{id}")
//    public ResponseEntity<Void> deleteBook(@PathVariable("id") long id) {
//        bookService.deleteById(id);
//        return ResponseEntity.ok().build();
////        return "redirect:/books";
//    }
}
