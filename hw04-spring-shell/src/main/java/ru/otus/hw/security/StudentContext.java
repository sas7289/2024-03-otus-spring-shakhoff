package ru.otus.hw.security;

import ru.otus.hw.domain.Student;

public interface StudentContext {

    void login(String firstName, String lastName);

    boolean isStudentLoggedIn();

    Student getStudent();
}
