package com.common.models.skills;

import com.common.models.enums.SkillLevel;
import dev.morphia.annotations.Embedded;

@Embedded
public class Foraging extends Skill {
    public Foraging() {
        super();
    }

    public Foraging(SkillLevel level, int xp) {
        super(level, xp);
    }

    @Override
    public void learn() {

    }

    @Override
    public String toString() {
        return "Foraging";
    }
}
