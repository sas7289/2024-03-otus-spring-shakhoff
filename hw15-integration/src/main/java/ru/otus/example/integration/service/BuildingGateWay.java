package ru.otus.example.integration.service;

import java.util.List;
import ru.otus.example.integration.model.Building;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface BuildingGateWay {

    @Gateway(requestChannel = "documentChannel", replyChannel = "buildingChannel")
    Building process(List<String> document);
}
