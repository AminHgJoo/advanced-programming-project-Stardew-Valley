package com.common.models;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.AssetManager;
import com.common.models.enums.types.inventoryEnums.BackpackType;
import com.common.models.items.Fish;
import com.common.models.items.Seed;
import com.common.models.items.Tool;
import com.common.models.items.TreeSeed;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Backpack {
    private ArrayList<Slot> slots = new ArrayList<>();
    private BackpackType type;
    //TODO load
    private Texture texture;

    public Backpack() {

    }

    public Backpack(BackpackType type) {
        this.type = type;
        this.texture = AssetManager.getImage("backpack");
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
        output.append("Filled slots: ");
        output.append(slots.size()).append("\n");
        output.append("Maximum slots: ");
        output.append(type.getMaxCapacity()).append("\n");
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

    public Slot findSeedByItemName(String itemName) {
        for (Slot slot : slots) {
            if (slot.getItem() instanceof Seed) {
                if (slot.getItem().getName().compareToIgnoreCase(itemName) == 0) {
                    return slot;
                }
            }
        }
        return null;
    }

    public Slot findTreeSeedByItemName(String itemName) {
        for (Slot slot : slots) {
            if (slot.getItem() instanceof TreeSeed) {
                if (slot.getItem().getName().compareToIgnoreCase(itemName) == 0) {
                    return slot;
                }
            }
        }
        return null;
    }


    public Slot getFirstFish() {
        for (Slot slot : slots) {
            if (slot.getItem() instanceof Fish) {
                return slot;
            }
        }
        return null;
    }
}
