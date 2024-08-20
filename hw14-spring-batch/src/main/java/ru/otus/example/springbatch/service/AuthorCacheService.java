package ru.otus.example.springbatch.service;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.example.springbatch.model.AuthorMongo;

@Service
@Getter
@Slf4j
public class AuthorCacheService {
    List<AuthorMongo> authors = new ArrayList<>();

    public AuthorMongo cache(AuthorMongo authorMongo) {
        log.info("add author " + authorMongo.getFullName());
        authors.add(authorMongo);
        return authorMongo;
    }
}
