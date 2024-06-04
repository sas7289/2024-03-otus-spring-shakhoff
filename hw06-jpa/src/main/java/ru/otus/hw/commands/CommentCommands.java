package ru.otus.hw.commands;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentServiceImpl;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentServiceImpl commentService;

    private final BookConverter bookConverter;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findById(long id) {
        return commentService.findById(id)
                .map(commentConverter::commentDtotoString)
                .orElse("Comment with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Find comments by book id", key = "cbbid")
    public String findByBookId(long bookId) {
        return commentService.findByBookId(bookId).stream()
            .map(commentConverter::commentDtotoString)
            .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // cins newComment 1 1,6
    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String content, long bookId) {
        var savedComment = commentService.insert(content, bookId, LocalDateTime.now(Clock.systemUTC()));
        return commentConverter.commentDtotoString(savedComment);
    }

    // cupd 4 editedComment 3 "Content" 5
    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateBook(long id, String content, long bookId) {
        var updatedComment = commentService.update(id, content, bookId);
        return commentConverter.commentDtotoString(updatedComment);
    }

    // cdel 4
    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteBook(long id) {
        commentService.deleteById(id);
    }
}
