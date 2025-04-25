package com.example.models.skills;

import com.example.models.enums.SkillLevel;
import dev.morphia.annotations.Embedded;

@Embedded
public class Farming extends Skill {
    @Override
    public void learn() {

    }

    public Farming() {
        super();
    }

    public Farming(SkillLevel level, int xp) {
        super(level, xp);
    }

    @Override
    public String toString() {
        return "Farming";
    }
}
