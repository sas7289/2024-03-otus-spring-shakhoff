package ru.otus.hw.converters;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BaseBookDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Genre;

@RequiredArgsConstructor
@Component
public class BookConverter {

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;


    public String bookDtoToString(BookDTO bookDto) {
        var genresString = bookDto.getGenres().stream()
            .map(genreConverter::genreDtoToString)
            .map("{%s}"::formatted)
            .collect(Collectors.joining(", "));

        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
            bookDto.getId(),
            bookDto.getTitle(),
            authorConverter.authorDtoToString(bookDto.getAuthor()),
            genresString);
    }

    public BookDTO toDto(Book book, Author author, List<Genre> genres) {
        List<GenreDTO> genreDtos = genres.stream().map(genreConverter::toDto).toList();
        AuthorDTO authorDTO = authorConverter.toDto(author);
        return new BookDTO(book.getId(), book.getTitle(), authorDTO, genreDtos);
    }

    public BaseBookDTO toDto(Book book) {
       return new BaseBookDTO(book.getId(), book.getTitle(), book.getAuthorId(), book.getGenreIds());
    }
}
