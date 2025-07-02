package com.common.models.skills;

import com.common.models.enums.SkillLevel;
import dev.morphia.annotations.Embedded;

@Embedded
public class Mining extends Skill {
    @Override
    public void learn() {

    }

    public Mining() {
        super();
    }

    public Mining(SkillLevel level, int xp) {
        super(level, xp);
    }

    @Override
    public String toString() {
        return "Mining";
    }
}
