package ru.otus.example.integration.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import lombok.RequiredArgsConstructor;
import ru.otus.example.integration.model.Building;
import ru.otus.example.integration.model.Document;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.example.integration.model.Room;

@Service
@RequiredArgsConstructor
public class BuildingService {

    private static final String[] DOCUMENTS = {"Reactor Building", "Turbine Hall", "Administrative and Service Areas", "Nuclear Fuel Storage",
        "Control Room", "Waste Processing and Storage", "Emergency Cooling and Safety Systems", "Energy Systems and Pump Stations",
        "Chemical Reagent Storage", "Technical and Auxiliary Facilities"};

    private final BuildingGateWay gateWay;

    public void startBuilding() {
        List<Document> documents = Arrays.stream(DOCUMENTS)
            .map(this::prepareDocument)
            .toList();
                Building process = gateWay.process(documents);
//                    System.out.println("_______________________________BUILDING SIZE: " + process.getRooms().size());
        for (Room room : process.getRooms()) {
            System.out.println("ROOM: " + room.getName());
        }
    }

    private Document prepareDocument(String documentName) {
        return new Document(documentName, getRandomWidth(), getRandomLength());
    }

    private int getRandomWidth() {
        return RandomUtils.nextInt(2, 5);
    }

    private int getRandomLength() {
        return RandomUtils.nextInt(5, 10);
    }
}
