package com.example.models;

import com.example.models.enums.types.itemTypes.*;
import dev.morphia.annotations.Embedded;

@Embedded
public class Quest {
    private int count;
    private boolean completed = false;
    private String fieldName = "";
    private String enumName = "";

    public Quest() {

    }

    public Quest(ItemType item, int count) {
        this.fieldName = item.name();
        if (item instanceof CropSeedsType) {
            enumName = "CropSeedsType";
        } else if (item instanceof MiscType) {
            enumName = "MiscType";
        } else if (item instanceof FoodTypes) {
            enumName = "FoodTypes";
        } else if (item instanceof ForagingMineralsType) {
            enumName = "ForagingMineralsType";
        } else if (item instanceof FishType) {
            enumName = "FishType";
        }
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ItemType getItem() {
        if (enumName.equals("CropSeedsType")) {
            return CropSeedsType.valueOf(fieldName);
        } else if (enumName.equals("MiscType")) {
            return MiscType.valueOf(fieldName);
        } else if (enumName.equals("FoodTypes")) {
            return FoodTypes.valueOf(fieldName);
        } else if (enumName.equals("ForagingMineralsType")) {
            return ForagingMineralsType.valueOf(fieldName);
        } else if (enumName.equals("FishType")) {
            return FishType.valueOf(fieldName);
        }
        return null;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    @Override
    public String toString() {
        return fieldName + " " + count;
    }
}
