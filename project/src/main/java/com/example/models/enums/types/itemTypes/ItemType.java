package com.example.models.enums.types.itemTypes;

import com.example.models.Slot;
import dev.morphia.annotations.Embedded;

@Embedded
public interface ItemType {
    Slot createAmountOfItem(int amount);
}
