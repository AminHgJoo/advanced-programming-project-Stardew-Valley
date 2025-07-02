package com.common.models.skills;

import com.common.models.enums.SkillLevel;
import dev.morphia.annotations.Embedded;

@Embedded
public class Farming extends Skill {
    public Farming() {
        super();
    }

    public Farming(SkillLevel level, int xp) {
        super(level, xp);
    }

    @Override
    public void learn() {
    }

    @Override
    public String toString() {
        return "Farming";
    }
}
