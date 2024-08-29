package ru.otus.example.springbatch.config;

import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.example.springbatch.model.AuthorJpa;
import ru.otus.example.springbatch.model.AuthorMongo;
import ru.otus.example.springbatch.model.BookJpa;
import ru.otus.example.springbatch.model.GenreJpa;
import ru.otus.example.springbatch.model.GenreMongo;

@Configuration
public class ReaderConfig {

    @StepScope
    @Bean
    public JpaPagingItemReader<BookJpa> jpaBookReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<BookJpa>()
            .name("jpaBookReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("select b from BookJpa b")
            .pageSize(100)
            .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<AuthorJpa> jpaAuthorReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<AuthorJpa>()
            .name("jpaAuthorReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("select a from AuthorJpa a")
            .pageSize(100)
            .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<GenreJpa> jpaGenreReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<GenreJpa>()
            .name("jpaGenreReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("select g from GenreJpa g")
            .pageSize(100)
            .build();
    }

    @StepScope
    @Bean
    public MongoPagingItemReader<AuthorMongo> mongoAuthorReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<AuthorMongo>()
            .name("mongoAuthorReader")
            .template(mongoTemplate)
            .collection("authors")
            .jsonQuery("{}")
            .targetType(AuthorMongo.class)
            .pageSize(100)
            .sorts(new HashMap<>())
            .build();
    }

    @StepScope
    @Bean
    public MongoPagingItemReader<GenreMongo> mongoGenreReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<GenreMongo>()
            .name("mongoGenreReader")
            .template(mongoTemplate)
            .collection("genres")
            .jsonQuery("{}")
            .targetType(GenreMongo.class)
            .pageSize(100)
            .sorts(new HashMap<>())
            .build();
    }

}
