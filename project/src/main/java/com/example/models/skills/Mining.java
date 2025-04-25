package com.example.models.skills;

import dev.morphia.annotations.Embedded;

@Embedded
public class Mining extends Skill {
    @Override
    public void learn() {

    }

    public Mining() {
        super();
    }

    @Override
    public String toString() {
        return "Mining";
    }
}
