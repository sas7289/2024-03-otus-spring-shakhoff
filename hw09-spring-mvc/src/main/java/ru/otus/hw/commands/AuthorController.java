package ru.otus.hw.commands;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.services.AuthorService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class AuthorController {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

//    @ShellMethod(value = "Find all authors", key = "aa")
    @GetMapping("/authors")
    public String findAllAuthors(Model model) {
        List<AuthorDTO> authors = authorService.findAll();
//            .collect(Collectors.joining("," + System.lineSeparator()));
        model.addAttribute("authors", authors);
        return "";
    }
}
