package com.client.utils;

public enum FacingDirection {
    DOWN(0),
    RIGHT(1),
    UP(2),
    LEFT(3);

    private final int animationRow;

    FacingDirection(int row) {
        this.animationRow = row;
    }

    public int getAnimationRow() {
        return animationRow;
    }
}
