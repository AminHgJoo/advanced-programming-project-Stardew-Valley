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

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
