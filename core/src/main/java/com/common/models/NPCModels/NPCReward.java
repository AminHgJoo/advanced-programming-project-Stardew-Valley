package com.common.models.NPCModels;

import com.common.models.enums.types.itemTypes.FoodTypes;
import com.common.models.enums.types.itemTypes.ForagingMineralsType;
import com.common.models.enums.types.itemTypes.ItemType;
import com.common.models.enums.types.itemTypes.MiscType;
import dev.morphia.annotations.Embedded;

@Embedded
public class NPCReward {
    public int money;
    public int friendshipLevel;
    public int count;
    private String fieldName;
    private String enumName;

    public NPCReward(int money, ItemType rewardItems) {
        this.money = money;
        fieldName = rewardItems == null ? null : rewardItems.name();
        if (rewardItems instanceof FoodTypes) {
            enumName = "FoodTypes";
        } else if (rewardItems instanceof MiscType) {
            enumName = "MiscTypes";
        } else if (rewardItems instanceof ForagingMineralsType) {
            enumName = "ForagingMinerals";
        }
    }

    public NPCReward() {
    }

    public NPCReward(int money, ItemType rewardItems, int friendshipLevel, int count) {
        this.money = money;
        fieldName = rewardItems == null ? null : rewardItems.name();
        if (rewardItems instanceof FoodTypes) {
            enumName = "FoodTypes";
        } else if (rewardItems instanceof MiscType) {
            enumName = "MiscTypes";
        } else if (rewardItems instanceof ForagingMineralsType) {
            enumName = "ForagingMinerals";
        }
        this.friendshipLevel = friendshipLevel;
        this.count = count;
    }

    public ItemType getRewardItems() {
        if (fieldName == null) {
            return null;
        } else if (enumName.trim().equals("FoodTypes")) {
            return FoodTypes.valueOf(fieldName);
        } else if (enumName.equals("MiscTypes")) {
            return MiscType.valueOf(fieldName);
        } else if (enumName.equals("ForagingMinerals")) {
            return ForagingMineralsType.valueOf(fieldName);
        }
        return null;
    }
}
