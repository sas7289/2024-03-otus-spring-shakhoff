package ru.otus.example.integration.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.example.integration.service.BuildingService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {
	final BuildingService buildingService;

	@Override
	public void run(String... args) {
		buildingService.startBuilding();
	}
}
