package ru.otus.hw.service;

import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

public interface ResultService {

    void showResult(TestResult testResult);

    void saveResult(Student student, TestResult testResult);

    TestResult getResult(Student student);
}
