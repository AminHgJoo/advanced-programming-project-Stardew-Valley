package com.example.models.NPCModels;

import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class NPCReward {
    public int money;
    public ItemType rewardItems;
    public int friendshipLevel;
    public int count;

    public NPCReward(int money, ItemType rewardItems) {
        this.money = money;
        this.rewardItems = rewardItems;
    }

    public NPCReward(){

    }

    public NPCReward(int money, ItemType rewardItems, int friendshipLevel , int count) {
        this.money = money;
        this.rewardItems = rewardItems;
        this.friendshipLevel = friendshipLevel;
        this.count = count;
    }
}
