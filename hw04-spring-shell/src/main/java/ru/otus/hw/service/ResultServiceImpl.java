package ru.otus.hw.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestConfig testConfig;

    private final LocalizedIOService ioService;

    private final Map<Student, TestResult> studentTestResults = new HashMap<>();

    @Override
    public void showResult(TestResult testResult) {
        ioService.printLine("");
        ioService.printLineLocalized("ResultService.test.results");
        ioService.printFormattedLineLocalized("ResultService.student",
            testResult.getStudent().getFullName());
        ioService.printFormattedLineLocalized("ResultService.answered.questions.count",
            testResult.getAnsweredQuestions().size());
        ioService.printFormattedLineLocalized("ResultService.right.answers.count",
            testResult.getRightAnswersCount());

        if (testResult.getRightAnswersCount() >= testConfig.getRightAnswersCountToPass()) {
            ioService.printLineLocalized("ResultService.passed.test");
            return;
        }
        ioService.printLineLocalized("ResultService.fail.test");
    }

    @Override
    public void saveResult(Student student, TestResult testResult) {
        this.studentTestResults.put(student, testResult);
    }

    @Override
    public TestResult getResult(Student student) {
        return this.studentTestResults.get(student);
    }
}
