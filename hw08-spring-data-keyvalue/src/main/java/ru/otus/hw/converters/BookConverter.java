package ru.otus.hw.converters;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;

@RequiredArgsConstructor
@Component
public class BookConverter {

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public String bookDtoToString(BookDTO bookDto) {
        var genresString = bookDto.getGenres().stream()
            .map(genreConverter::genreDtoToString)
            .map("{%s}"::formatted)
            .collect(Collectors.joining(", "));

        var commetsString = bookDto.getComments().stream()
            .map(commentConverter::commentDtotoString)
            .collect(Collectors.joining(", "));
        return "Id: %s, title: %s, author: {%s}, genres: [%s], comments: [%s]".formatted(
            bookDto.getId(),
            bookDto.getTitle(),
            authorConverter.authorDtoToString(bookDto.getAuthor()),
            genresString,
            commetsString);
    }

    public BookDTO toDto(Book book) {
        List<GenreDTO> genreDtos = book.getGenres().stream().map(genreConverter::toDto).toList();
        AuthorDTO authorDTO = authorConverter.toDto(book.getAuthor());
        List<CommentDTO> commentDTOS = ListUtils.emptyIfNull(book.getComments()).stream()
            .map(commentConverter::toDto)
            .toList();
        return new BookDTO(book.getId(), book.getTitle(), authorDTO, genreDtos, commentDTOS);
    }

}
