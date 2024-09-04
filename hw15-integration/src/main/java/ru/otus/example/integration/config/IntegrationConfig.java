package ru.otus.example.integration.config;

import java.util.List;
import java.util.concurrent.Executors;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import ru.otus.example.integration.model.Building;
import ru.otus.example.integration.model.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.example.integration.model.Room;
import ru.otus.example.integration.service.RoomService;

@Configuration
public class IntegrationConfig {

//    @Bean
//    public MessageChannelSpec<?, ?> documentChannel() {
//        return MessageChannels.executor(Executors.newCachedThreadPool());
//    }

    @Bean
    public MessageChannelSpec<?, ?> documentChannel() {
        return MessageChannels.queue(10);
    }

//    @Bean
//    public MessageChannelSpec<?, ?> roomChannel() {
//        return MessageChannels.queue(10);
//    }

    @Bean
    public MessageChannelSpec<?, ?> buildingChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow buildingFlow(RoomService roomService) {
        return IntegrationFlow.from(documentChannel())
            .split()
            .handle(roomService, "build")
            .aggregate(
//                a -> a
//                .correlationStrategy(message -> 1)
//                .releaseStrategy(group -> group.size() == 5)
//                .expireGroupsUponCompletion(true)
            )
            .transform(List.class, payload -> new Building("",(List<Room>) payload))
//            .channel(buildingChannel())
            .get();
    }

    @Bean
    @ServiceActivator(inputChannel = "buildingChannel")
    public MessageHandler consoleOutputHandler() {
        return message -> {
//                System.out.println("Сообщение: " + ((Building) message.getPayload()).getRooms().size());
            System.out.println("Сообщение: " + message.getPayload().getClass());
            for (Room room : ((Building) message.getPayload()).getRooms()) {
                System.out.println("ROOM: " + room.getName());
            }
        };
    }

}
