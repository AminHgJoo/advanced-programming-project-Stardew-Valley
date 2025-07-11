package com.common.models.enums.types.itemTypes;

import com.common.models.Slot;
import com.common.models.enums.Quality;
import dev.morphia.annotations.Embedded;

@Embedded
public interface ItemType {
    Slot createAmountOfItem(int amount, Quality quality);

    String getName();

    String name();

    String getTextureName();
}
