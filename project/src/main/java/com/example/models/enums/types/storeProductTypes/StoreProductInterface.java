package com.example.models.enums.types.storeProductTypes;

import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.worldEnums.Season;

public interface StoreProductInterface {
    public String getDescription();

    public int getPrice();

    public double getOutOfSeasonPrice();

    public double getDailyLimit();

    public Season[] getSeasons();

    public String getName();

    public ItemType getItemType();
}
