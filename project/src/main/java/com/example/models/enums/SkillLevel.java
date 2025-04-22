package com.example.models.enums;

public enum SkillLevel {
    FOUR(null, 1, 4),
    THREE(FOUR, 0, 3),
    TWO(THREE, 0, 2),
    ONE(TWO, 0, 1),
    ZERO(ONE, 0, 0);

    final public double xpToNextLevel;
    final public SkillLevel nextLevel;
    final public int energyCostDiscount;
    final public int levelNumber;

    SkillLevel(SkillLevel nextLevel, int energyCostDiscount, int levelNumber) {
        this.energyCostDiscount = energyCostDiscount;
        this.levelNumber = levelNumber;
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
