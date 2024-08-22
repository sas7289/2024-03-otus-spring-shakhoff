package ru.otus.example.springbatch.config;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.example.springbatch.config.JobConfig.H2_TO_MONGO_JOB_NAME;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.example.springbatch.model.AuthorJpa;
import ru.otus.example.springbatch.model.BookJpa;
import ru.otus.example.springbatch.model.BookMongo;
import ru.otus.example.springbatch.model.GenreJpa;

@SpringBootTest
@SpringBatchTest
class JobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void clearData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test void test() throws Exception {
        jobLauncherTestUtils.getJob();
        System.out.println("tst");

        List<BookJpa> jpaBooks = entityManager.createQuery("select b from BookJpa b order by b.title", BookJpa.class).getResultList();
        List<BookJpa> expectedBooks = prepareBooks();

        assertThat(jpaBooks)
            .usingRecursiveComparison()
            .isEqualTo(expectedBooks);

        List<BookMongo> mongoBooksBeforeJobExecution = mongoTemplate.findAll(BookMongo.class);
        assertThat(mongoBooksBeforeJobExecution)
            .isEmpty();


        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
            .extracting(Job::getName)
            .isEqualTo(H2_TO_MONGO_JOB_NAME);

        JobParameters parameters = new JobParametersBuilder()
            .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
        List<BookMongo> all = mongoTemplate.findAll(BookMongo.class);


        assertThat(all)
            .usingRecursiveComparison()
            .ignoringFields("id", "author.id", "genres.id")
            .isEqualTo(expectedBooks);
        all.size();
    }


    private List<BookJpa> prepareBooks() {
        AuthorJpa author1 = new AuthorJpa(1, "Author_1");
        AuthorJpa author2 = new AuthorJpa(2, "Author_2");

        GenreJpa genre1 = new GenreJpa(1, "Genre_1");
        GenreJpa genre2 = new GenreJpa(2, "Genre_2");

        BookJpa book1 = new BookJpa(1, "BookTitle_1", author1, List.of(genre1));
        BookJpa book2 = new BookJpa(2, "BookTitle_2", author2, List.of(genre2));
        return List.of(book1, book2);
    }
}