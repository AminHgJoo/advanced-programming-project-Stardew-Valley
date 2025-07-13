package com.client.utils;

public enum FacingDirection {
    DOWN(0),
    LEFT(1),
    RIGHT(2),
    UP(3);

    private final int animationRow;

    FacingDirection(int row) {
        this.animationRow = row;
    }

    public int getAnimationRow() {
        return animationRow;
    }
}
