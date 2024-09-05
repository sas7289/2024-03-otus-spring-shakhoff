package ru.otus.example.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.example.integration.model.EngineeringDocument;

@ExtendWith(MockitoExtension.class)
class EngineeringDocumentServiceTest {
    private final EngineeringDocumentService engineeringDocumentService = new EngineeringDocumentService();

    @Test
    @DisplayName("Должен возвращать корректный инженерный документ")
    void shouldReturnCorrectEngineeringDocument() {
        String documentName = "document1";
        EngineeringDocument engineeringDocument = engineeringDocumentService.prepareDocument(documentName);

        assertThat(engineeringDocument).isNotNull();
        assertThat(engineeringDocument.getRoomName()).isEqualTo(documentName);
        assertThat(engineeringDocument.getLength()).isNotEqualTo(0);
        assertThat(engineeringDocument.getWidth()).isNotEqualTo(0);
    }
}