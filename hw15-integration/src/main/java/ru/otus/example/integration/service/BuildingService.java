package ru.otus.example.integration.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.otus.example.integration.model.Building;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuildingService {

    private static final String[] DOCUMENTS = {"Reactor Building", "Turbine Hall", "Administrative and Service Areas",
        "Nuclear Fuel Storage", "Control Room", "Waste Processing and Storage", "Emergency Cooling and Safety Systems",
        "Energy Systems and Pump Stations", "Chemical Reagent Storage", "Technical and Auxiliary Facilities"};

    private final BuildingGateWay gateWay;

    public Building startBuilding() {
        return gateWay.process(List.of(DOCUMENTS));
    }
}
