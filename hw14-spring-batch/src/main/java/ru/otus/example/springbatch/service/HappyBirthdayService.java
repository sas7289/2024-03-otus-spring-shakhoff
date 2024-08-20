package ru.otus.example.springbatch.service;

import org.springframework.stereotype.Service;
import ru.otus.example.springbatch.model.Book;
import ru.otus.example.springbatch.model.Person;

@Service
public class HappyBirthdayService {

    public Book doHappyBirthday(Book person){
//        person.setAge(person.getAge() + 1);
        return person;
    }
}
