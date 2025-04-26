package com.example.models;

import com.example.models.enums.types.inventoryEnums.BackpackType;
import com.example.models.items.Seed;
import com.example.models.items.Tool;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Backpack {
    private ArrayList<Slot> slots = new ArrayList<>();
    private BackpackType type;

    public Backpack() {

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

    public void addSlot(Slot slot) {
        this.slots.add(slot);
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

    public Tool getWateringCan() {
        for (Slot slot : slots) {
            if (slot.getItem() instanceof Tool) {
                if (((Tool) slot.getItem()).getType().waterCapacity > 0) {
                    return (Tool) slot.getItem();
                }
            }
        }
        return null;
    }

    public ArrayList<Seed> getSeeds() {
        ArrayList<Seed> seeds = new ArrayList<>();
        for (Slot slot : slots) {
            if (slot.getItem() instanceof Seed) {
                seeds.add((Seed) slot.getItem());
            }
        }
        return seeds;
    }

    public Seed findSeedByItemName(String itemName) {
        for (Slot slot : slots) {
            if (slot.getItem() instanceof Seed) {
                if (slot.getItem().getName().compareToIgnoreCase(itemName) == 0) {
                    return (Seed) slot.getItem();
                }
            }
        }
        return null;
    }

}
