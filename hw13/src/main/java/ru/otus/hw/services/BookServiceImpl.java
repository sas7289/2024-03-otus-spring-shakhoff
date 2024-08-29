package ru.otus.hw.services;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    private final CommentRepository commentRepository;

    private final PermissionService permissionService;


    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    public BookDTO findById(long id) {
        return bookRepository.findById(id).map(bookConverter::toDto)
            .orElseThrow(() -> new EntityNotFoundException("Book not found by id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<BookDTO> findAll() {
        List<Book> allBooks = bookRepository.findAll();
        return allBooks.stream().map(bookConverter::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDTO insert(String title, long authorId, Set<Long> genresIds) {
        BookDTO savedBook = bookConverter.toDto(save(0, title, authorId, genresIds));
        ObjectIdentityImpl objectIdentity = new ObjectIdentityImpl(savedBook);
        GrantedAuthoritySid roleAdmin = new GrantedAuthoritySid("ROLE_admin");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalSid owner = new PrincipalSid(authentication);
        permissionService.addPermissionForAuthority(BasePermission.READ, objectIdentity, owner, roleAdmin);
        return savedBook;
    }

    @Override
    @Transactional
    public BookDTO update(long id, String title, long authorId, Set<Long> genresIds) {
        Book updatedBook = save(id, title, authorId, genresIds);
        return bookConverter.toDto(updatedBook);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        List<Comment> comments = commentRepository.findByBookId(id);

        var book = new Book(id, title, author, genres, comments);
        return bookRepository.save(book);
    }
}
