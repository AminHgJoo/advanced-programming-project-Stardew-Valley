package com.example.models;

import com.example.models.enums.types.BackpackType;
import com.example.models.items.Tool;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Backpack {
    private ArrayList<Slot> slots = new ArrayList<>();
    private BackpackType type;

    public Backpack(){

    }
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
            if (slot.getItem().getName().compareToIgnoreCase(itemName) == 0) {
                return slot;
            }
        }
        return null;
    }

    public void removeSlot(Slot slot) {
        slots.remove(slot);
    }

    public String showInventory() {
        StringBuilder output = new StringBuilder();
        for (Slot slot : slots) {
            output.append(slot.toString()).append("\n");
        }
        return output.toString();
    }

    public String showTools() {
        StringBuilder tools = new StringBuilder();
        for (Slot slot : slots) {
            if (slot.getItem() instanceof Tool)
                tools.append(slot.toString()).append("\n");
        }
        return tools.toString();
    }

}
