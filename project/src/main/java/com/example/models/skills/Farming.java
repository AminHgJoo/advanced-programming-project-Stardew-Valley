package com.example.models.skills;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;

@Embedded
public class Farming extends Skill {
    @Override
    public void learn() {

    }

    public Farming() {
        super();
    }

    @Override
    public String toString() {
        return "Farming";
    }
}
