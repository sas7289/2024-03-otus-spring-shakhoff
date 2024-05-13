package ru.otus.hw.service;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    public static final String ANSWER_TEMPLATE = "\t%s. %s";

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        questionDao.findAll().forEach(q -> {
            ioService.printLine(q.text());

            String answers = IntStream.range(0, q.answers().size())
                .mapToObj(answerIndex ->
                    String.format(ANSWER_TEMPLATE, answerIndex + 1, q.answers().get(answerIndex).text()))
                .collect(Collectors.joining("\n"));
            ioService.printLine(answers);
        });
    }
}
