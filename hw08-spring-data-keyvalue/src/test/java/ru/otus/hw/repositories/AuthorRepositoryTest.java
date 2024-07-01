package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.core.SpringVersion;

@DataMongoTest
class AuthorRepositoryTest {

    @Test
    void test() {
        System.out.println(SpringVersion.getVersion());
    }

}