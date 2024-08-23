package ru.otus.example.springbatch.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.example.springbatch.model.AuthorMongo;
import ru.otus.example.springbatch.model.BookMongo;
import ru.otus.example.springbatch.model.GenreMongo;

@Configuration
public class WriterConfig {

    @StepScope
    @Bean
    public MongoItemWriter<BookMongo> mongoBookWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<BookMongo>()
            .template(mongoTemplate)
            .collection("books")
            .build();
    }

    @StepScope
    @Bean
    public MongoItemWriter<AuthorMongo> mongoAuthorWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<AuthorMongo>()
            .template(mongoTemplate)
            .collection("authors")
            .build();
    }

    @StepScope
    @Bean
    public MongoItemWriter<GenreMongo> mongoGenreWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<GenreMongo>()
            .template(mongoTemplate)
            .collection("genres")
            .build();
    }
}
