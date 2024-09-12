package ru.otus.example.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.example.integration.model.EngineeringDocument;
import ru.otus.example.integration.model.Room;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    private final RoomService roomService = new RoomService();

    @Test
    @DisplayName("Должен возвращать помещение в соответствии с переданым документом")
    void shouldReturnCorrectRoom() {
        String documentName = "documentName";
        int width = 5;
        int length = 10;
        EngineeringDocument engineeringDocument = new EngineeringDocument(documentName, width, length);
        Room room = roomService.build(engineeringDocument);
        assertThat(room.getName()).isEqualTo(documentName);
        assertThat(room.getSquare()).isEqualTo(width * length);
    }
}