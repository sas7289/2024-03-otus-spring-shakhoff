package ru.otus.hw.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.domain.Student;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    public static final String STUDENT_FIRST_NAME = "firstName";
    public static final String STUDENT_LAST_NAME = "lastName";
    @Mock
    private IOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @DisplayName("Должен возвращать ожидаемый объект Student")
    @Test
    void shouldReturnExpectedStudent() {
        Student expectedStudent = prepareExistStudent();

        given(ioService.readStringWithPrompt(any())).willReturn(STUDENT_FIRST_NAME).willReturn(STUDENT_LAST_NAME);
        Student student = studentService.determineCurrentStudent();
        verify(ioService, times(2)).readStringWithPrompt(any());

        Assertions.assertEquals(expectedStudent, student);
    }

    private Student prepareExistStudent() {
        return new Student(STUDENT_FIRST_NAME, STUDENT_LAST_NAME);
    }
}