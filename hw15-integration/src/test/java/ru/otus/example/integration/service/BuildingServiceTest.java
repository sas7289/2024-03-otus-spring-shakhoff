package ru.otus.example.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.example.integration.model.Building;
import ru.otus.example.integration.model.Room;

@SpringBootTest
class BuildingServiceTest {

    @Autowired
    private BuildingService buildingService;

    private final String[] DOCUMENTS = {"Reactor Building", "Turbine Hall", "Administrative and Service Areas", "Nuclear Fuel Storage",
        "Control Room", "Waste Processing and Storage", "Emergency Cooling and Safety Systems", "Energy Systems and Pump Stations",
        "Chemical Reagent Storage", "Technical and Auxiliary Facilities"};

    private final String BUILDING_NAME = "Kudankulam";


    @Test
    void test() {
        Building building = buildingService.startBuilding();
        assertThat(building.getName()).isEqualTo(BUILDING_NAME);
        assertThat(building.getRooms().size()).isEqualTo(10);
        assertThat(building)
            .extracting(b -> b.getRooms().stream().map(Room::getName).toList())
            .isEqualTo(Arrays.asList(DOCUMENTS));
    }
}