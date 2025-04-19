package com.example.models;

import com.example.models.enums.types.BackpackType;

import java.util.ArrayList;

public class Backpack {
    private final ArrayList<Slot> slots = new ArrayList<>();
    private BackpackType type;

    public Backpack(BackpackType type) {
        this.type = type;
    }

    public ArrayList<Slot> getSlots() {
        return slots;
    }

    public BackpackType getType() {
        return type;
    }

    public void setType(BackpackType type) {
        this.type = type;
    }

    public Slot getSlotByItemName(String itemName) {
        for (Slot slot : slots) {
            if (slot.getHeader().getName().equals(itemName)) {
                return slot;
            }
        }
        return null;
    }

    public void removeSlot(Slot slot) {
        slots.remove(slot);
    }

}
