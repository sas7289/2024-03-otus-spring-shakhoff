package ru.otus.example.springbatch.config;

import java.util.List;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.example.springbatch.model.AuthorJpa;
import ru.otus.example.springbatch.model.AuthorMongo;
import ru.otus.example.springbatch.model.BookJpa;
import ru.otus.example.springbatch.model.BookMongo;
import ru.otus.example.springbatch.model.GenreJpa;
import ru.otus.example.springbatch.model.GenreMongo;
import ru.otus.example.springbatch.service.AuthorCacheService;
import ru.otus.example.springbatch.service.GenreCacheService;

@Configuration
public class ProcessorConfig {

    @Bean
    public ItemProcessor<AuthorJpa, AuthorMongo> authorResetIdProcessor() {
        return item -> new AuthorMongo(null, item.getFullName());
    }

    @Bean
    public ItemProcessor<GenreJpa, GenreMongo> genreResetIdProcessor() {
        return item -> new GenreMongo(null, item.getName());
    }

    @Bean
    public ItemProcessor<GenreMongo, GenreMongo> genreCacheProcessor(GenreCacheService cacheService) {
        return cacheService::cache;
    }

    @Bean
    public ItemProcessor<BookJpa, BookMongo> bookProcessor(AuthorCacheService authorCacheService, GenreCacheService genreCacheService) {
        return item -> {
            AuthorMongo authorMongo = authorCacheService.getAuthors().stream()
                .filter(author -> author.getFullName().equals(item.getAuthor().getFullName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("AuthorJpa not found"));

            List<String> bookGenres = item.getGenres().stream()
                .map(GenreJpa::getName)
                .toList();

            List<GenreMongo> genreMongos = genreCacheService.getGenres().stream()
                .filter(genre -> bookGenres.contains(genre.getName()))
                .toList();

            return new BookMongo(null, item.getTitle(), authorMongo, genreMongos);
        };
    }

    @Bean
    public ItemProcessor<AuthorMongo, AuthorMongo> authorCacheProcessor(AuthorCacheService cacheService) {
        return cacheService::cache;
    }

}
