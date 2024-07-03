package ru.otus.hw.commands;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentServiceImpl;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentServiceImpl commentService;

    private final BookConverter bookConverter;

    private final CommentConverter commentConverter;

    //    @ShellMethod(value = "Find comment by id", key = "cbid")
    @GetMapping("/comment/{id}")
    public String findById(@PathVariable("id") long id) {
        return commentService.findById(id)
            .map(commentConverter::commentDtotoString)
            .orElse("Comment with id %d not found".formatted(id));
    }

    //    @ShellMethod(value = "Find comments by book id", key = "cbbid")
    @GetMapping("/comment")
    public String findByBookId(@RequestParam long bookId) {
        return commentService.findByBookId(bookId).stream()
            .map(commentConverter::commentDtotoString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // cins newComment 1 1,6
//    @ShellMethod(value = "Insert comment", key = "cins")
    @PostMapping("/comment")
    public String insertComment(@RequestParam String content, @RequestParam long bookId, RedirectAttributes redirectAttributes) {
        var savedComment = commentService.insert(content, bookId, LocalDateTime.now(Clock.systemUTC()));
        redirectAttributes.addAttribute("id", bookId);
        return "redirect:books/{id}";
    }

    // cupd 4 editedComment 3 "Content" 5
//    @ShellMethod(value = "Update comment", key = "cupd")
    @PostMapping("/comment/update")
    public String updateBook(@RequestParam long id, @RequestParam String content, @RequestParam long bookId) {
        var updatedComment = commentService.update(id, content, bookId);
        return commentConverter.commentDtotoString(updatedComment);
    }

    // cdel 4
//    @ShellMethod(value = "Delete comment by id", key = "cdel")
    @DeleteMapping("comment/{id}")
    public void deleteBook(@PathVariable("id") long id) {
        commentService.deleteById(id);
    }
}
