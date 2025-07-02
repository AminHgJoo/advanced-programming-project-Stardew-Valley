package com.common.models.skills;

import com.common.models.enums.SkillLevel;
import dev.morphia.annotations.Embedded;

@Embedded
public class Fishing extends Skill {
    public Fishing() {
        super();
    }

    public Fishing(SkillLevel level, int xp) {
        super(level, xp);
    }

    @Override
    public void learn() {

    }

    @Override
    public String toString() {
        return "Fishing";
    }
}
