package ru.otus.hw.rest;

import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.hw.services.CommentServiceImpl;

@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping("/comment")
    public String insertComment(@RequestParam String content,
                                @RequestParam long bookId,
                                RedirectAttributes redirectAttributes) {
        var savedComment = commentService.insert(content, bookId, LocalDateTime.now(Clock.systemUTC()));
        redirectAttributes.addAttribute("id", bookId);
        return "redirect:books/{id}";
    }
}
