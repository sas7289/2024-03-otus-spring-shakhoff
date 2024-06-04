package ru.otus.hw.services;

import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreService {
    List<GenreDTO> findAll();
}
