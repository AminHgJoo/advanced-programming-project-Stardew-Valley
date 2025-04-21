package com.example.models.enums;

public enum SkillLevel {
    FOUR(null, 1),
    THREE(FOUR, 0),
    TWO(THREE, 0),
    ONE(TWO, 0),
    ZERO(ONE, 0);

    final public double xpToNextLevel;
    final public SkillLevel nextLevel;
    final public int energyCostDiscount;

    SkillLevel(SkillLevel nextLevel, int energyCostDiscount) {
        this.energyCostDiscount = energyCostDiscount;
        if (nextLevel == null) {
            this.xpToNextLevel = Double.POSITIVE_INFINITY;
        } else {
            this.xpToNextLevel = 100 * (5 - this.ordinal()) + 50;
        }
        this.nextLevel = nextLevel;
    }

    public SkillLevel getNextLevel() {
        return nextLevel;
    }

    public double getXpToNextLevel() {
        return xpToNextLevel;
    }
}
