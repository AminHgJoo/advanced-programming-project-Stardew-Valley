package com.example.models.mapObjects;

import com.example.models.Animal;

public class AnimalBlock extends MapObject {
    public Animal animal;
    public AnimalBlock(Animal animal) {
        super(false, "animal", "purple");
        this.animal = animal;
    }
}
