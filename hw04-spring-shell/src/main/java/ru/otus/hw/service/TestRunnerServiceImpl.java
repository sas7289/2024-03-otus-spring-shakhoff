package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.otus.hw.security.StudentContext;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService, CommandLineRunner {

    private final TestService testService;

    private final StudentContext studentContext;

    private final ResultService resultService;

    @Override
    public void run(String... args) {
        run();
    }

    @Override
    public void run() {
        var testResult = testService.executeTestFor(studentContext.getStudent());
        resultService.saveResult(studentContext.getStudent(), testResult);
    }
}
