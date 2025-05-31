package com.example.models.buildings;

import com.example.models.Animal;
import com.example.models.mapModels.Cell;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Barn extends Building {
    public String barnType;
    public ArrayList<Animal> animals = new ArrayList<>();
    public int capacity;

    public Barn() {
    }

    public Barn(ArrayList<Cell> buildingCells, String barnType, int capacity) {
        super(buildingCells);
        this.barnType = barnType;
        this.capacity = capacity;
    }
}
