package com.example.models.NPCModels;

import models.Player;

public class NPCFriendship {
    private final Player player;
    private final NPC npc;
    private final int level;

    public NPCFriendship(Player player, NPC npc, int level) {
        this.player = player;
        this.npc = npc;
        this.level = level;
    }

    public Player getPlayer() {
        return player;
    }

    public NPC getNpc() {
        return npc;
    }

    public int getLevel() {
        return level;
    }
}
