package com.example.models;

import dev.morphia.annotations.Embedded;

@Embedded
public class PlayerAnimal {
    private Animal animal;
    private int xp;

    public PlayerAnimal() {

    }

    public PlayerAnimal(Animal animal, int xp) {
        this.animal = animal;
        this.xp = xp;
    }
}
