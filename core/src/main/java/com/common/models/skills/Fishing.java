package com.common.models.skills;

import com.common.models.enums.SkillLevel;
import dev.morphia.annotations.Embedded;

@Embedded
public class Fishing extends Skill {
    @Override
    public void learn() {

    }

    public Fishing() {
        super();
    }

    public Fishing(SkillLevel level, int xp) {
        super(level, xp);
    }

    @Override
    public String toString() {
        return "Fishing";
    }
}
