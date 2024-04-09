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

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printLine(question.text());

            IntStream.range(0, question.answers().size())
                .forEach(value -> ioService.printFormattedLine("\t%s. %s", value + 1, question.answers().get(value).text()));

            int answerNumber = ioService.readIntForRange(1, question.answers().size(),
                "The entered answer number does not correspond to the proposed answers. Enter a valid number");
            testResult.applyAnswer(question, question.answers().get(answerNumber - 1).isCorrect());
        }
        return testResult;
    }
}
