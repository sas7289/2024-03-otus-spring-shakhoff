package ru.otus.example.integration.config;

import java.util.List;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageHandler;
import ru.otus.example.integration.model.Building;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.example.integration.model.Room;
import ru.otus.example.integration.service.EngineeringDocumentService;
import ru.otus.example.integration.service.RoomService;

@Configuration
public class IntegrationConfig {


    @Bean
    public MessageChannelSpec<?, ?> documentChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> buildingChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow buildingFlow(EngineeringDocumentService engineeringDocumentService, RoomService roomService) {
        return IntegrationFlow.from(documentChannel())
            .split()
            .handle(engineeringDocumentService, "prepareDocument")
            .handle(roomService, "build")
            .aggregate()
            .transform(List.class, payload -> new Building("Kudankulam", (List<Room>) payload))
            .get();
    }

    @Bean
    @ServiceActivator(inputChannel = "buildingChannel")
    public MessageHandler consoleOutputHandler() {
        return message -> {
            System.out.println("Building : " + message.getPayload().getClass());
            for (Room room : ((Building) message.getPayload()).getRooms()) {
                System.out.println("ROOM: " + room.getName());
            }
        };
    }
}
