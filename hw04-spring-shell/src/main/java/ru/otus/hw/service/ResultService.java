package ru.otus.hw.service;

import java.util.Set;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

public interface ResultService {

    void showResult(TestResult testResult);

    void saveResult(Student student, TestResult testResult);

    Set<TestResult> getResults(Student student);
}
