package com.common.models;

import dev.morphia.annotations.Embedded;

@Embedded
public class Friendship {
    private String player;
    private int level;
    private int xp;

    public Friendship() {

    }

    public Friendship(String player) {
        this.player = player;
        this.level = 0;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
        int diff = this.xp - (level + 1) * 100;
        if (diff > 0) {
            this.xp = diff;
            this.level += 1;
        }
    }
}
