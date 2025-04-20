package com.example.models.skills;

import com.example.models.enums.SkillLevel;
import dev.morphia.annotations.Embedded;

@Embedded
abstract public class Skill {
    protected double energyCost;
    protected SkillLevel level;
    protected int xp;

    abstract public void learn();
}
