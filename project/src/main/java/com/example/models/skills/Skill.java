package com.example.models.skills;

import models.enums.SkillLevel;

abstract public class Skill {
    protected double energyCost;
    protected SkillLevel level;
    protected int xp;

    abstract public void learn();
}
