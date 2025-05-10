package com.example.models.enums;

import com.example.models.NPCModels.NPCRequest;
import com.example.models.NPCModels.NPCReward;
import com.example.models.items.Item;

public enum NPC {
    SEBASTIAN("Sebastian", null, new NPCRequest(null), new NPCReward(5000, null)),
    ABIGAIL("Abigail", null, new NPCRequest(null), new NPCReward(500, null)),
    HARVEY("Harvey", null, new NPCRequest(null), new NPCReward(750, null)),
    LYA("Lya", null, new NPCRequest(null), new NPCReward(500, null)),
    ROBIN("Robin", null, new NPCRequest(null), new NPCReward(25000, null)),
    CLINT("Clint", null, null, null),
    MORRIS("Morris", null, null, null),
    PIERRE("Pierre", null, null, null),
    WILLY("Willy", null, null, null),
    MARNIE("Marnie", null, null, null),
    GUS("Gus", null, null, null),
    ;
    public final String name;
    public final Item[] items;
    public final NPCRequest request;
    public final NPCReward reward;

    NPC(String name, Item[] items, NPCRequest request, NPCReward reward) {
        this.name = name;
        this.items = items;
        this.request = request;
        this.reward = reward;
    }
}
