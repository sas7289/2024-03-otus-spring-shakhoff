package ru.otus.example.integration.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Building {

    private String name;
    private List<Room> rooms;


    public Building(String name, List<Room> rooms) {
        this.name = name;
        this.rooms = rooms;
    }
}
