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
import ru.otus.example.springbatch.model.Author;
import ru.otus.example.springbatch.model.AuthorMongo;
import ru.otus.example.springbatch.model.Book;
import ru.otus.example.springbatch.model.BookMongo;
import ru.otus.example.springbatch.model.Genre;
import ru.otus.example.springbatch.model.GenreMongo;
import ru.otus.example.springbatch.service.AuthorCacheService;
import ru.otus.example.springbatch.service.CleanUpService;
import ru.otus.example.springbatch.service.GenreCacheService;


@SuppressWarnings("unused")
@Configuration
public class JobConfig {

    private static final int CHUNK_SIZE = 5;
    private final Logger logger = LoggerFactory.getLogger("Batch");

    public static final String OUTPUT_FILE_NAME = "outputFileName";
    public static final String INPUT_FILE_NAME = "inputFileName";
    public static final String IMPORT_USER_JOB_NAME = "importUserJob";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;


    @Autowired
    private CleanUpService cleanUpService;

//    @StepScope
//    @Bean
//    public FlatFileItemReader<Person> reader(@Value("#{jobParameters['" + INPUT_FILE_NAME + "']}") String inputFileName) {
//        return new FlatFileItemReaderBuilder<Person>()
//                .name("personItemReader")
//                .resource(new ClassPathResource(inputFileName))
//
//                .delimited()
//                .names("name", "age")
//                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
//                    setTargetType(Person.class);
//                }}).build();
//    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Book> jpaBookReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Book>()
            .name("jpaBookReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("select b from Book b")
            .pageSize(100)
            .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Author> jpaAuthorReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Author>()
            .name("jpaAuthorReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("select a from Author a")
            .pageSize(100)
            .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Genre> jpaGenreReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Genre>()
            .name("jpaGenreReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("select g from Genre g")
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
    public ItemProcessor<Author, AuthorMongo> authorResetIdProcessor() {
        return item -> new AuthorMongo(null, item.getFullName());
    }

    @Bean
    public ItemProcessor<Genre, GenreMongo> genreResetIdProcessor() {
        return item -> new GenreMongo(null, item.getName());
    }

    @Bean
    public ItemProcessor<GenreMongo, GenreMongo> genreCacheProcessor(GenreCacheService cacheService) {
        return cacheService::cache;
    }

    @Bean
    public ItemProcessor<Book, BookMongo> bookProcessor(AuthorCacheService authorCacheService, GenreCacheService genreCacheService) {
        return item -> {
            AuthorMongo authorMongo = authorCacheService.getAuthors().stream()
                .filter(author -> author.getFullName().equals(item.getAuthor().getFullName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Author not found"));

            List<String> bookGenres = item.getGenres().stream()
                .map(Genre::getName)
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

//    @StepScope
//    @Bean
//    public FlatFileItemWriter<Person> writer(@Value("#{jobParameters['" + OUTPUT_FILE_NAME + "']}") String outputFileName) {
//        return new FlatFileItemWriterBuilder<Person>()
//                .name("personItemWriter")
//                .resource(new FileSystemResource(outputFileName))
//                .lineAggregator(new DelimitedLineAggregator<>())
//                .build();
//    }


    @Bean
    public MethodInvokingTaskletAdapter cleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }


    @Bean
    public Job transferAuthorJob(Step transferAuthorStep, Step transferGenreStep, Step cleanUpStep, Step cacheAuthorStep, Step cacheGenreStep, Step transferBookStep) {
        return new JobBuilder(IMPORT_USER_JOB_NAME, jobRepository)
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
    public Step transferAuthorStep(JpaPagingItemReader<Author> reader, MongoItemWriter<AuthorMongo> writer, ItemProcessor<Author, AuthorMongo> authorResetIdProcessor) {
        return new StepBuilder("transferAuthorStep", jobRepository)
            .<Author, AuthorMongo>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(authorResetIdProcessor)
            .writer(writer)
//                .taskExecutor(new SimpleAsyncTaskExecutor())
            .build();
    }

    @Bean
    public Step transferGenreStep(JpaPagingItemReader<Genre> reader, MongoItemWriter<GenreMongo> writer, ItemProcessor<Genre, GenreMongo> genreResetIdProcessor) {
        return new StepBuilder("transferGenreStep", jobRepository)
            .<Genre, GenreMongo>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(genreResetIdProcessor)
            .writer(writer)
//                .taskExecutor(new SimpleAsyncTaskExecutor())
            .build();
    }

    @Bean
    public Step transferBookStep(JpaPagingItemReader<Book> reader, MongoItemWriter<BookMongo> writer, ItemProcessor<Book, BookMongo> bookProcessor) {
        return new StepBuilder("transferBookStep", jobRepository)
            .<Book, BookMongo>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(reader)
            .processor(bookProcessor)
            .writer(writer)
//                .taskExecutor(new SimpleAsyncTaskExecutor())
            .build();
    }

    @Bean
    public Step cacheAuthorStep(MongoPagingItemReader<AuthorMongo> mongoAuthorReader, ItemProcessor<AuthorMongo, AuthorMongo> authorCacheProcessor) {
        return new StepBuilder("cacheAuthorStep", jobRepository)
            .<AuthorMongo, AuthorMongo>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(mongoAuthorReader)
            .processor(authorCacheProcessor)
            .writer(chunk -> {})
//                .taskExecutor(new SimpleAsyncTaskExecutor())
            .build();
    }

    @Bean
    public Step cacheGenreStep(MongoPagingItemReader<GenreMongo> mongoGenreReader, ItemProcessor<GenreMongo, GenreMongo> genreCacheProcessor) {
        return new StepBuilder("cacheGenreStep", jobRepository)
            .<GenreMongo, GenreMongo>chunk(CHUNK_SIZE, platformTransactionManager)
            .reader(mongoGenreReader)
            .processor(genreCacheProcessor)
            .writer(chunk -> {})
//                .taskExecutor(new SimpleAsyncTaskExecutor())
            .build();
    }

//    @Bean
//    public Step testGenreStep(MongoPagingItemReader<GenreMongo> mongoGenreReader) {
//        return new StepBuilder("transformPersonsStep", jobRepository)
//            .<GenreMongo, GenreMongo>chunk(CHUNK_SIZE, platformTransactionManager)
//            .reader(mongoGenreReader)
//            .processor(item -> {
//                System.out.println(item.getName());
//                return item;
//            })
////            .writer(writer)
////                .taskExecutor(new SimpleAsyncTaskExecutor())
//            .build();
//    }

//    @Bean
//    public Step transformPersonsStep(JpaPagingItemReader<Book> reader, MongoItemWriter<Book> writer,
//                                     ItemProcessor<Book, Book> itemProcessor) {
//        return new StepBuilder("transformPersonsStep", jobRepository)
//                .<Book, Book>chunk(CHUNK_SIZE, platformTransactionManager)
//                .reader(reader)
//                .processor(itemProcessor)
//            .processor()
//                .writer(writer)
//                .listener(new ItemReadListener<>() {
//                    public void beforeRead() {
//                        logger.info("Начало чтения");
//                    }
//
//                    public void afterRead(@NonNull Book o) {
//                        logger.info("Конец чтения: " + o.getTitle());
//                    }
//
//                    public void onReadError(@NonNull Exception e) {
//                        logger.info("Ошибка чтения");
//                    }
//                })
//                .listener(new ItemWriteListener<Book>() {
//                    public void beforeWrite(@NonNull List<Book> list) {
//                        logger.info("Начало записи");
//                    }
//
//                    public void afterWrite(@NonNull List<Book> list) {
//                        logger.info("Конец записи");
//                    }
//
//                    public void onWriteError(@NonNull Exception e, @NonNull List<Book> list) {
//                        logger.info("Ошибка записи");
//                    }
//                })
//                .listener(new ItemProcessListener<>() {
//                    public void beforeProcess(@NonNull Book o) {
//                        logger.info("Начало обработки");
//                    }
//
//                    public void afterProcess(@NonNull Book o, Book o2) {
//                        logger.info("Конец обработки");
//                    }
//
//                    public void onProcessError(@NonNull Book o, @NonNull Exception e) {
//                        logger.info("Ошибка обработки");
//                    }
//                })
//                .listener(new ChunkListener() {
//                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
//                        logger.info("Начало пачки");
//                    }
//
//                    public void afterChunk(@NonNull ChunkContext chunkContext) {
//                        logger.info("Конец пачки");
//                    }
//
//                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
//                        logger.info("Ошибка пачки");
//                    }
//                })
////                .taskExecutor(new SimpleAsyncTaskExecutor())
//                .build();
//    }

    @Bean
    public Step cleanUpStep(AuthorCacheService authorCacheService) {
        return new StepBuilder("cleanUpStep", jobRepository)
            .tasklet(cleanUpTasklet(), platformTransactionManager)
            .listener(new StepExecutionListener() {
                @Override
                public ExitStatus afterStep(StepExecution stepExecution) {
                    authorCacheService.getAuthors().forEach(author ->  logger.info("add author " + author.getFullName()));
                    return StepExecutionListener.super.afterStep(stepExecution);
                }
            })
            .build();
    }
}
