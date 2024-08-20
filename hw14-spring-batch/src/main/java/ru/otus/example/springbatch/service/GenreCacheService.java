package ru.otus.example.springbatch.service;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.example.springbatch.model.AuthorMongo;
import ru.otus.example.springbatch.model.GenreMongo;

@Service
@Getter
@Slf4j
public class GenreCacheService {
    List<GenreMongo> authors = new ArrayList<>();

    public GenreMongo cache(GenreMongo genreMongo) {
        log.info("add genre " + genreMongo.getName());
        authors.add(genreMongo);
        return genreMongo;
    }
}
