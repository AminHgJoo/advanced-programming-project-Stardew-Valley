package com.example.models;

import com.example.controllers.gameMenuControllers.TradingController;
import com.example.models.items.Item;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

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
    public boolean tradeResult;

    public Trade(){
    }

}
