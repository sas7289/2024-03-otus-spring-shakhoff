package ru.otus.example.springbatch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.example.springbatch.model.AuthorJpa;
import ru.otus.example.springbatch.model.AuthorMongo;
import ru.otus.example.springbatch.model.BookJpa;
import ru.otus.example.springbatch.model.BookMongo;
import ru.otus.example.springbatch.model.GenreJpa;
import ru.otus.example.springbatch.model.GenreMongo;


@Configuration
public class JobConfig {

    private static final int CHUNK_SIZE = 5;
    private final Logger logger = LoggerFactory.getLogger("Batch");

    public static final String H2_TO_MONGO_JOB_NAME = "h2ToMongoJob";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;


    @Bean
    public Job transferBooksJob(Step transferAuthorStep, Step transferGenreStep, Step cacheAuthorStep, Step cacheGenreStep,
                                Step transferBookStep) {
        return new JobBuilder(H2_TO_MONGO_JOB_NAME, jobRepository)
            .incrementer(new RunIdIncrementer())
            .flow(transferAuthorStep)
            .next(transferGenreStep)
            .next(cacheAuthorStep)
            .next(cacheGenreStep)
            .next(transferBookStep)
            .end()
            .build();
    }

    @Bean
    public Step transferAuthorStep(JpaPagingItemReader<AuthorJpa> reader, MongoItemWriter<AuthorMongo> writer,
                                   ItemProcessor<AuthorJpa, AuthorMongo> authorResetIdProcessor) {
        return new StepBuilder("transferAuthorStep", jobRepository)
            .<AuthorJpa, AuthorMongo>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(authorResetIdProcessor)
            .writer(writer)
            .build();
    }

    @Bean
    public Step transferGenreStep(JpaPagingItemReader<GenreJpa> reader, MongoItemWriter<GenreMongo> writer,
                                  ItemProcessor<GenreJpa, GenreMongo> genreResetIdProcessor) {
        return new StepBuilder("transferGenreStep", jobRepository)
            .<GenreJpa, GenreMongo>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(genreResetIdProcessor)
            .writer(writer)
            .build();
    }

    @Bean
    public Step transferBookStep(JpaPagingItemReader<BookJpa> reader, MongoItemWriter<BookMongo> writer,
                                 ItemProcessor<BookJpa, BookMongo> bookProcessor) {
        return new StepBuilder("transferBookStep", jobRepository)
            .<BookJpa, BookMongo>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(bookProcessor)
            .writer(writer)
            .build();
    }

    @Bean
    public Step cacheAuthorStep(MongoPagingItemReader<AuthorMongo> mongoAuthorReader, ItemProcessor<AuthorMongo, AuthorMongo> authorCacheProcessor) {
        return new StepBuilder("cacheAuthorStep", jobRepository)
            .<AuthorMongo, AuthorMongo>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(mongoAuthorReader)
            .processor(authorCacheProcessor)
            .writer(chunk -> {
            })
            .build();
    }

    @Bean
    public Step cacheGenreStep(MongoPagingItemReader<GenreMongo> mongoGenreReader, ItemProcessor<GenreMongo, GenreMongo> genreCacheProcessor) {
        return new StepBuilder("cacheGenreStep", jobRepository)
            .<GenreMongo, GenreMongo>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(mongoGenreReader)
            .processor(genreCacheProcessor)
            .writer(chunk -> {
            })
            .build();
    }
}
