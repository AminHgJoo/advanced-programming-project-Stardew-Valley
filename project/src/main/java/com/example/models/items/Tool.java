package com.example.models.items;

import java.util.HashMap;

public class Tool extends Item {
    private final HashMap<String, String> properties = new HashMap<>();

    public void upgradeTool() {

    }

    @Override
    public void useItem() {

    }

    @Override
    public void deleteItem() {

    }

    @Override
    public void dropItem() {

    }

    public HashMap<String, String> getProperties() {
        return properties;
    }
}
