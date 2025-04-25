package com.example.models.NPCModels;

import com.example.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class NPCReward {
    public int money;
    public Item[] rewardItems;

    public NPCReward(int money, Item[] rewardItems) {
        this.money = money;
        this.rewardItems = rewardItems;
    }
}
