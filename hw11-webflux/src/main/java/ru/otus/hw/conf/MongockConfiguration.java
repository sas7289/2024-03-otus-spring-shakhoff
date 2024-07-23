package ru.otus.hw.conf;

import com.mongodb.reactivestreams.client.MongoClient;
import io.mongock.driver.api.driver.ConnectionDriver;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongockConfiguration {

    @Bean
    public ConnectionDriver connectionDriver(MongoClient mongoClient) {
        MongoReactiveDriver otusHw08 = MongoReactiveDriver.withDefaultLock(mongoClient, "otus_hw_08");
        otusHw08.disableTransaction();
        return otusHw08;
    }

}
