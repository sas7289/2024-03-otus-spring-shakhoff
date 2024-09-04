package ru.otus.example.integration.service;

import lombok.NoArgsConstructor;
import ru.otus.example.integration.model.Document;
import ru.otus.example.integration.model.Room;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class RoomService {

    public Room build(Document document) {
        return new Room(document.getRoomName(), Math.multiplyExact(document.getLength(), document.getLength()));
    }

}
