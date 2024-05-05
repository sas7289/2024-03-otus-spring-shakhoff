package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class ResultServiceImplTest {

    @Autowired
    private ResultService resultService;

    @Test
    public void shouldReturnTestResultByStudent() {
        Student student_1 = new Student("firstName1", "lastName1");
        Student student_2 = new Student("firstName2", "lastName2");

        TestResult result_1 = new TestResult(student_1);
        TestResult result_2 = new TestResult(student_2);
        resultService.saveResult(student_1, result_1);
        resultService.saveResult(student_2, result_2);

        Assertions.assertEquals(result_1, resultService.getResult(student_1));
        Assertions.assertEquals(result_2, resultService.getResult(student_2));
    }
}