package com.example.models.skills;

import com.example.models.enums.SkillLevel;
import dev.morphia.annotations.Embedded;

@Embedded
public class Foraging extends Skill {
    @Override
    public void learn() {

    }

    public Foraging() {
        super();
    }

    public Foraging(SkillLevel level, int xp) {
        super(level, xp);
    }

    @Override
    public String toString() {
        return "Foraging";
    }
}
