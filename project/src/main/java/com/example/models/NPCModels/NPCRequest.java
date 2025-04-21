package com.example.models.NPCModels;

import com.example.models.items.Item;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class NPCRequest {
    private Item[] requestedItems;

    public NPCRequest(Item[] requestedItems) {
        this.requestedItems = requestedItems;
    }

    public Item[] getRequestedItems() {
        return requestedItems;
    }

    public void setRequestedItems(Item[] requestedItems) {
        this.requestedItems = requestedItems;
    }
}
