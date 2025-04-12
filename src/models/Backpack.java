package models;

import models.enums.BackpackType;

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
}
