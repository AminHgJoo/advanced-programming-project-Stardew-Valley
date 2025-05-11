package com.example.models;

import com.example.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class Gift {
    private String from;
    private String to;
    private Item item;
    private int amount;
    private int rate = 0;

    public Gift() {

    }

    public Gift(String from, String to, Item item, int amount) {
        this.from = from;
        this.to = to;
        this.item = item;
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
