package com.example.models;

import dev.morphia.annotations.Embedded;

@Embedded
public class Friendship {
    private Player player;
    private int level;
    private int xp;

    public Friendship(){

    }
    public Friendship(Player player) {
        this.player = player;
        this.level = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
