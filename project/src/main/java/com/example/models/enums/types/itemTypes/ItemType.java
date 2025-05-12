package com.example.models.enums.types.itemTypes;

import com.example.models.Slot;
import com.example.models.enums.Quality;
import dev.morphia.annotations.Embedded;

@Embedded
public interface ItemType {
    Slot createAmountOfItem(int amount, Quality quality);

    String getName();

    String name();
}
