package models;

import models.items.Item;

public class Slot {
    private final Item header;
    private int count;

    public Slot(Item header, int count) {
        this.header = header;
        this.count = count;
    }

    public Item getHeader() {
        return header;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
