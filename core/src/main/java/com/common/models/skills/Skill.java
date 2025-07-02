package com.common.models.skills;

import com.common.models.enums.SkillLevel;
import dev.morphia.annotations.Embedded;

@Embedded
abstract public class Skill {
    protected SkillLevel level;
    protected int xp;

    protected Skill() {
        this.level = SkillLevel.ZERO;
        this.xp = 0;
    }

    protected Skill(SkillLevel level, int xp) {
        this.level = level;
        this.xp = xp;
    }

    abstract public void learn();

    public int getXp() {
        return this.xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public SkillLevel getLevel() {
        return this.level;
    }

    public void setLevel(SkillLevel level) {
        this.level = level;
    }
}
