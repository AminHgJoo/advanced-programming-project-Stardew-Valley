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

    public String getDirection() {
        String direction = null;
        if (this == FacingDirection.UP) {
            direction = "up";
        } else if (this == FacingDirection.DOWN) {
            direction = "down";
        } else if (this == FacingDirection.LEFT) {
            direction = "left";
        } else if (this == FacingDirection.RIGHT) {
            direction = "right";
        }
        return direction;
    }
}
