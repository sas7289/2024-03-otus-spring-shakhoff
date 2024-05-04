package ru.otus.hw.security;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Student;

import static java.util.Objects.nonNull;

@Component
public class InMemoryStudentContext implements StudentContext {

    private Student student;

    @Override
    public void login(String firstName, String lastName) {
        this.student = new Student(firstName, lastName);
    }

    @Override
    public boolean isStudentLoggedIn() {
        return nonNull(student);
    }

    @Override
    public Student getStudent() {
        return this.student;
    }
}
