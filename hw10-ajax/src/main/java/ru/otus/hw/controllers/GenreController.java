package ru.otus.hw.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
@RestController
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public List<GenreDTO> findAllBooks() {
        return genreService.findAll();
    }
}
