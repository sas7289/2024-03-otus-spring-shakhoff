package ru.otus.hw.controllers;

import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.services.CommentServiceImpl;

@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping("/comment")
    public Mono<CommentDTO> insertComment(@RequestParam String content,
                                          @RequestParam String bookId) {
        return commentService.insert(content, bookId, LocalDateTime.now(Clock.systemUTC()));
    }
}
