package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("authors-entity-graph");
        Map<String, Object> properties = Map.of(EntityGraphType.FETCH.getKey(), entityGraph);
        return Optional.ofNullable(entityManager.find(Book.class, id, properties));
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("authors-entity-graph");
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b", Book.class);
        query.setHint(EntityGraphType.FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(book -> {
            Query query = entityManager.createQuery("delete from Book b where b.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
        });
    }


    private Book insert(Book book) {
        entityManager.persist(book);
        return book;
    }

    private Book update(Book book) {
        return entityManager.merge(book);
    }
}
