package ru.otus.hw.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.services.AuthorService;

@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public List<AuthorDTO> findAllBooks() {
        return authorService.findAll();
    }
}
