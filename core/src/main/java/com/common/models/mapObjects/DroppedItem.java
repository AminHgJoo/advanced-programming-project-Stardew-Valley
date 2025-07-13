package com.common.models.mapObjects;

import com.client.utils.AssetManager;
import com.common.models.enums.types.itemTypes.*;
import com.common.models.items.Item;
import dev.morphia.annotations.Embedded;

@Embedded
public class DroppedItem extends MapObject {
    private int quantity;
    private Item item;

    public DroppedItem() {
        super();
    }

    public DroppedItem(int quantity, Item item) {
        super(true, "droppedItem", "red", null);
        this.quantity = quantity;
        this.item = item;
        ItemType itemType = getItemType(item.getName());
        this.texture = AssetManager.getImage(itemType.getTextureName());
    }

    public ItemType getItemType(String name) {
        for (CropSeedsType type : CropSeedsType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        for (FishType type : FishType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        for (FoodTypes type : FoodTypes.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        for (ForagingMineralsType type : ForagingMineralsType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        for (MiscType type : MiscType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        for (ToolTypes type : ToolTypes.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        for (TreeSeedsType type : TreeSeedsType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public int getQuantity() {
        return quantity;
    }

    public Item getItem() {
        return item;
    }
}
