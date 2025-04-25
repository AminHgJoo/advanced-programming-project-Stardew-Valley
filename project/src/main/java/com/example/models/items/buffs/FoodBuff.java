package com.example.models.items.buffs;

import dev.morphia.annotations.Embedded;

@Embedded
public class FoodBuff {
    private int increment;
    private String affectedField;

    public FoodBuff(String affectedField, int increment) {
        this.affectedField = affectedField;
        this.increment = increment;
    }

    public int getIncrement() {
        return increment;
    }

    public String getAffectedField() {
        return affectedField;
    }
}
