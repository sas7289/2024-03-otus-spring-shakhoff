package ru.otus.example.springbatch.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Properties;

import static ru.otus.example.springbatch.config.JobConfig.H2_TO_MONGO_JOB_NAME;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {
    private final JobOperator jobOperator;

    @SuppressWarnings("unused")
    @ShellMethod(value = "data migration from H2 database to MongoDB", key = "sm-hm")
    public void migrationH2ToMongo() throws Exception {
        Long executionId = jobOperator.start(H2_TO_MONGO_JOB_NAME, new Properties());
        System.out.println(jobOperator.getSummary(executionId));
    }
}
