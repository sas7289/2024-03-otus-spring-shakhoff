package ru.otus.example.springbatch.config;

import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.example.springbatch.model.AuthorJpa;
import ru.otus.example.springbatch.model.AuthorMongo;
import ru.otus.example.springbatch.model.BookJpa;
import ru.otus.example.springbatch.model.BookMongo;
import ru.otus.example.springbatch.model.GenreJpa;
import ru.otus.example.springbatch.model.GenreMongo;
import ru.otus.example.springbatch.service.AuthorCacheService;
import ru.otus.example.springbatch.service.CleanUpService;
import ru.otus.example.springbatch.service.GenreCacheService;


@SuppressWarnings("unused")
@Configuration
public class JobConfig {

    private static final int CHUNK_SIZE = 5;
    private final Logger logger = LoggerFactory.getLogger("Batch");

    public static final String H2_TO_MONGO_JOB_NAME = "h2ToMongoJob";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;


    @Autowired
    private CleanUpService cleanUpService;


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

    @Bean
    public MethodInvokingTaskletAdapter cleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }


    @Bean
    public Job transferAuthorJob(Step transferAuthorStep, Step transferGenreStep, Step cleanUpStep, Step cacheAuthorStep, Step cacheGenreStep,
                                 Step transferBookStep) {
        return new JobBuilder(H2_TO_MONGO_JOB_NAME, jobRepository)
            .incrementer(new RunIdIncrementer())
            .flow(transferAuthorStep)
            .next(transferGenreStep)
            .next(cacheAuthorStep)
            .next(cacheGenreStep)
            .next(transferBookStep)
            .next(cleanUpStep)
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

    @Bean
    public Step cleanUpStep(AuthorCacheService authorCacheService) {
        return new StepBuilder("cleanUpStep", jobRepository)
            .tasklet(cleanUpTasklet(), platformTransactionManager)
            .listener(new StepExecutionListener() {
                @Override
                public ExitStatus afterStep(StepExecution stepExecution) {
                    authorCacheService.getAuthors().forEach(author -> logger.info("add author " + author.getFullName()));
                    return StepExecutionListener.super.afterStep(stepExecution);
                }
            })
            .build();
    }
}
