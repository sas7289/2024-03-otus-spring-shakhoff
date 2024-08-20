package ru.otus.example.springbatch;

import com.github.cloudyrock.spring.v5.EnableMongock;
import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

@EnableMongock
@SpringBootApplication
public class Main {
    // --spring.shell.interactive.enabled=false --spring.batch.job.enabled=true inputFileName=entries.csv outputFileName=output_new.dat
    public static void main(String[] args) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + new ClassPathResource("entries.csv").exists());
        SpringApplication.run(Main.class, args);
    }
}


