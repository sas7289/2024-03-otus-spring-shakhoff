package ru.otus.example.integration.service;

import lombok.NoArgsConstructor;
import ru.otus.example.integration.model.EngineeringDocument;
import ru.otus.example.integration.model.Room;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class RoomService {

    public Room build(EngineeringDocument engineeringDocument) {
        return new Room(engineeringDocument.getRoomName(), Math.multiplyExact(engineeringDocument.getLength(), engineeringDocument.getWidth()));
    }
}
