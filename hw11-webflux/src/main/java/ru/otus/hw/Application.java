package ru.otus.hw;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableMongock
public class Application {

	/**
	 *
	 * для запуска фронта:
	 * 	1. выполнеить команду mvn package
	 * 	2. запустить Spring проект
	 *	3. запустить frontend, выполнив команду из корневой директории проекта в PowerShell
	 *	cd hw11-ajax\src\main\frontend;ng serve
	 * */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
