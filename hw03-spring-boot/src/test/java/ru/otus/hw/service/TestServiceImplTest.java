package ru.otus.hw.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    private final List<String> questionTexts = List.of("Question_1", "Question_2", "Question_3", "Question_4");
    private final List<String> answerTexts = List.of("Answer_1", "Answer_2", "Answer_3", "Answer_4");

    @DisplayName("Должен вывести текст каждого вопроса")
    @Test
    void shouldDisplayQuestionText() {
        Student student = prepareStudent();

        given(questionDao.findAll()).willReturn(prepareQuestions());
        given(ioService.readIntForRangeLocalized(anyInt(), anyInt(), any())).willReturn(1);

        testService.executeTestFor(student);

        var printLineCaptor = ArgumentCaptor.forClass(String.class);
        verify(ioService, times(6)).printLine(printLineCaptor.capture());
        Assertions.assertTrue(CollectionUtils.isSubCollection(questionTexts, printLineCaptor.getAllValues()));
    }

    @DisplayName("Должен вывести текст каждого варианта ответа")
    @Test
    void shouldDisplayAnswerText() {
        Student student = prepareStudent();

        given(questionDao.findAll()).willReturn(prepareQuestions());
        given(ioService.readIntForRangeLocalized(anyInt(), anyInt(), any())).willReturn(1);

        testService.executeTestFor(student);

        var printFormattedLineCaptor = ArgumentCaptor.forClass(String.class);
        verify(ioService, times(4)).printFormattedLine(any(), any(), printFormattedLineCaptor.capture());
        Assertions.assertTrue(CollectionUtils.isEqualCollection(answerTexts, printFormattedLineCaptor.getAllValues()));
    }

    private Student prepareStudent() {
        return new Student("FirstName", "LastName");
    }

    private List<Question> prepareQuestions() {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            List<Answer> answers = new ArrayList<>();
            answers.add(new Answer(answerTexts.get(i), false));
            questions.add(new Question(questionTexts.get(i), answers));

        }
        return questions;
    }
}