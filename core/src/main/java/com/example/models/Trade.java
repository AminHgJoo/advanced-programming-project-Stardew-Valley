package com.example.models;

import com.example.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class Trade {
    public Player firstPlayer;
    public Player secondPlayer;
    public String tradeType;
    public Item tradeItem;
    public int itemAmount;
    public int tradePrice;
    public Item targetItem;
    public int targetAmount;
    public int tradeResult;
    public boolean shown;
    public int id;

    public Trade() {
    }

    public Trade(Player firstPlayer, Player secondPlayer, String tradeType, Item item, int itemAmount, int tradePrice, Item targetItem, int targetAmount, int id) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.tradeType = tradeType;
        this.tradeItem = item;
        this.itemAmount = itemAmount;
        this.tradePrice = tradePrice;
        this.targetItem = targetItem;
        this.targetAmount = targetAmount;
        this.tradeResult = 0;
        this.id = id;
        this.shown = false;
    }

    @Override
    public String toString() {
        String result = "undecided";
        if (tradeResult == 1)
            result = "accepted";
        else if (tradeResult == 2)
            result = "rejected";
        if (tradeType.equals("offer"))
            return "Trade{" +
                    "id: " + id +
                    "from: " + firstPlayer +
                    ", to: " + secondPlayer +
                    ", tradeType: " + tradeType + '\'' +
                    ", tradeItem: " + tradeItem +
                    ", itemAmount: " + itemAmount +
                    ", tradePrice: " + tradePrice +
                    ", tradeResult: " + result +
                    '}';

        return "Trade{" +
                "id: " + id +
                "from: " + firstPlayer +
                ", to: " + secondPlayer +
                ", tradeType: " + tradeType + '\'' +
                ", tradeItem: " + tradeItem +
                ", itemAmount: " + itemAmount +
                ", targetItem: " + targetItem +
                ", targetAmount: " + targetAmount +
                ", tradeResult: " + result +
                '}';
    }
}
