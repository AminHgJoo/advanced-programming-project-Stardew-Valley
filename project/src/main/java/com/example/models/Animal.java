package com.example.models;

import com.example.models.enums.types.AnimalType;
import dev.morphia.annotations.Embedded;

@Embedded
public class Animal {
    private String name;
    private AnimalType type;
    private int cost;
    private int xp;
    public boolean hasBeenPetToDay;
    public boolean hasBeenFed;


    public Animal() {
    }

    public Animal(String name, AnimalType type, int cost) {
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.hasBeenPetToDay = false;
        this.hasBeenFed = false;
        this.xp = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnimalType getType() {
        return type;
    }

    public void setType(AnimalType type) {
        this.type = type;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}