package com.example.models.enums.types.storeProductTypes;

import com.example.models.enums.types.itemTypes.ItemType;
import com.example.models.enums.worldEnums.Season;
import dev.morphia.annotations.Embedded;

@Embedded
public interface StoreProductInterface {
    public String getDescription();

    public int getPrice();

    public double getOutOfSeasonPrice();

    public double getDailyLimit();

    public Season[] getSeasons();

    public String getName();

    public ItemType getItemType();

    default boolean isInSeason(Season season){
        Season[] seasons = getSeasons();
        boolean inSeason = false;
        for (Season s : seasons){
            if(s == season){
                inSeason = true;
            }
        }
        return inSeason;
    }
    default double getProductPrice(Season season){
        if(isInSeason(season)){
            return getPrice();
        }
        return getOutOfSeasonPrice();
    }
}
