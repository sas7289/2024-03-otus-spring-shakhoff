package ru.otus.example.integration.service;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.example.integration.model.EngineeringDocument;

@Service
@NoArgsConstructor
public class EngineeringDocumentService {

    public EngineeringDocument prepareDocument(String documentName) {
        return new EngineeringDocument(documentName, getRandomWidth(), getRandomLength());
    }

    private int getRandomWidth() {
        return RandomUtils.nextInt(2, 5);
    }

    private int getRandomLength() {
        return RandomUtils.nextInt(5, 10);
    }
}
