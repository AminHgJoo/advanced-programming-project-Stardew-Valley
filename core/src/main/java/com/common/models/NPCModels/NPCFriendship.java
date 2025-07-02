package com.common.models.NPCModels;

import dev.morphia.annotations.Embedded;

@Embedded
public class NPCFriendship {
    private String player;
    private String npc;
    private int level = 0;
    private int xp = 0;

    public NPCFriendship() {
    }

    public NPCFriendship(String player, String npc) {
        this.player = player;
        this.npc = npc;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getNpc() {
        return npc;
    }

    public void setNpc(String npc) {
        this.npc = npc;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
        this.level = xp / 200;
        if (level > 3) {
            level = 3;
            this.xp = 799;
        }
    }
}
