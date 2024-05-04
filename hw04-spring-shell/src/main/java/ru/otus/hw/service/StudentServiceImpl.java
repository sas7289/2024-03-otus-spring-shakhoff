package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.security.StudentContext;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentContext studentContext;

    @Override
    public Student determineCurrentStudent() {
        return studentContext.getStudent();
    }
}
