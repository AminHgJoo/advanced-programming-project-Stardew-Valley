package models.enums;

public enum SkillLevel {
    FOUR(null),
    THREE(FOUR),
    TWO(THREE),
    ONE(TWO),
    ZERO(ONE);

    final public double xpToNextLevel;
    final public SkillLevel nextLevel;

    SkillLevel(SkillLevel nextLevel) {
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
