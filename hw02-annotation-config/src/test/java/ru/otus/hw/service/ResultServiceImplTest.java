package ru.otus.hw.service;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@ExtendWith(MockitoExtension.class)
class ResultServiceImplTest {

    public static final String FIRST_NAME = "f";
    public static final String LAST_NAME = "l";
    public static final int PASS_TEST_STUDENT_RIGHT_ANSWERS_COUNT = 7;
    private static final int FAILURE_TEST_STUDENT_RIGHT_ANSWERS_COUNT = 3;
    @Mock
    private TestConfig testConfig;

    @Mock
    private IOService ioService;

    @InjectMocks
    private ResultServiceImpl resultService;

    private final String PASSED_TEST_EXPECTED_OUTPUT = "Congratulations! You passed test!";
    private final String FAILURE_TEST_EXPECTED_OUTPUT = "Sorry. You fail test.";
    private final int PASS_TEST_REQUIRED_ANSWERS_COUNT = 6;


    @DisplayName("Должен вывести о сдаче тестирования")
    @Test
    void shouldOutputCongratulationsOnPassingTest() {
        given(testConfig.getRightAnswersCountToPass()).willReturn(PASS_TEST_REQUIRED_ANSWERS_COUNT);
        var result = preparePassTestStudent();
        var captor = ArgumentCaptor.forClass(String.class);
        resultService.showResult(result);
        verify(ioService, times(3)).printLine(captor.capture());

        Assertions.assertEquals(PASSED_TEST_EXPECTED_OUTPUT, captor.getAllValues().get(captor.getAllValues().size() - 1));
    }

    @DisplayName("Должен вывести сообщение о несдаче тестирования")
    @Test
    void shouldOutputMessageAboutFailureTest() {
        given(testConfig.getRightAnswersCountToPass()).willReturn(PASS_TEST_REQUIRED_ANSWERS_COUNT);
        var result = prepareFailureTestStudent();
        var captor = ArgumentCaptor.forClass(String.class);
        resultService.showResult(result);
        verify(ioService, times(3)).printLine(captor.capture());

        Assertions.assertEquals(FAILURE_TEST_EXPECTED_OUTPUT, captor.getAllValues().get(captor.getAllValues().size() - 1));
    }

    private static TestResult preparePassTestStudent() {
        return prepareStudent(PASS_TEST_STUDENT_RIGHT_ANSWERS_COUNT);
    }

    private static TestResult prepareFailureTestStudent() {
        return prepareStudent(FAILURE_TEST_STUDENT_RIGHT_ANSWERS_COUNT);
    }

    private static TestResult prepareStudent(int rightAnswerCount) {
        var student = new TestResult(new Student(FIRST_NAME, LAST_NAME));
        student.setRightAnswersCount(rightAnswerCount);
        return student;
    }
}