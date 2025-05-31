package com.example.models.NPCModels;

import com.example.models.enums.types.itemTypes.FoodTypes;
import com.example.models.enums.types.itemTypes.ForagingMineralsType;
import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.types.itemTypes.MiscType;
import dev.morphia.annotations.Embedded;

@Embedded
public class FavoriteItem {
    private String fieldName;
    private String enumName;

    public FavoriteItem() {
    }

    public FavoriteItem(ItemType itemType) {
        this.fieldName = itemType == null ? null : itemType.name();
        if (itemType instanceof MiscType) {
            enumName = "MiscType";
        } else if (itemType instanceof FoodTypes) {
            enumName = "FoodTypes";
        } else if (itemType instanceof ForagingMineralsType) {
            enumName = "ForagingMinerals";
        }
    }

    public ItemType getItemType() {
        if (fieldName == null) return null;
        else if (enumName.equals("FoodTypes")) {
            return FoodTypes.valueOf(fieldName);
        } else if (enumName.equals("MiscType")) {
            return MiscType.valueOf(fieldName);
        } else if (enumName.equals("ForagingMinerals")) {
            return ForagingMineralsType.valueOf(fieldName);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteItem that = (FavoriteItem) o;
        if (fieldName != null ? !fieldName.equals(that.fieldName) : that.fieldName != null) return false;
        if (enumName != null ? !enumName.equals(that.enumName) : that.enumName != null) return false;
        return true;
    }
}
