package ru.otus.hw.service;

import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printLine(question.text());

            IntStream.range(0, question.answers().size())
                .forEach(value ->
                    ioService.printFormattedLine("\t%s. %s", value + 1, question.answers().get(value).text()));

            int answerNumber = ioService.readIntForRangeLocalized(1, question.answers().size(),
                "TestService.incorrect.input.message");

            testResult.applyAnswer(question, question.answers().get(answerNumber - 1).isCorrect());
        }
        return testResult;
    }
}
