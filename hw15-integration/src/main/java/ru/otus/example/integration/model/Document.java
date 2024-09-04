package ru.otus.example.integration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Document {

    private String roomName;
    private int width;
    private int length;
}
