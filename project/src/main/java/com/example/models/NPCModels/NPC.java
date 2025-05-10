package com.example.models.NPCModels;

import com.example.models.Quest;
import com.example.models.enums.types.itemTypes.*;
import com.example.models.items.*;
import com.example.models.mapModels.Coordinate;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;
import java.util.HashMap;

@Embedded
public class NPC {
    private String name;
    private ArrayList<ItemType> favoriteItems = new ArrayList<>();
    private ArrayList<NPCRequest> npcRequests = new ArrayList<>();
    private ArrayList<NPCReward> rewards = new ArrayList<>();
    private Coordinate coordinate;
    private ArrayList<NPCFriendship> friendships = new ArrayList<>();
    private ArrayList<Quest> quests = new ArrayList<>();
    private HashMap<String, Boolean> hasTalked = new HashMap<>();
    private HashMap<String, Boolean> gift = new HashMap<>();

    public NPC() {
    }

    public NPC(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
        initializeNpcQuest(name);
        initializeNpcFavorites(name);
        initializeNpcRewards(name);
    }

    public String getName() {
        return name;
    }

    public ArrayList<ItemType> getFavoriteItems() {
        return favoriteItems;
    }

    public ArrayList<NPCRequest> getNpcRequests() {
        return npcRequests;
    }

    public ArrayList<NPCReward> getRewards() {
        return rewards;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public ArrayList<NPCFriendship> getFriendships() {
        return friendships;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public void initializeNpcQuest(String name) {
        if (name.equals("Sebastian")) {
            quests.add(new Quest(ForagingMineralsType.IRON_ORE, 50));
            quests.add(new Quest(FoodTypes.PUMPKIN_PIE, 1));
            quests.add(new Quest(ForagingMineralsType.STONE, 150));
        } else if (name.equals("Abigale")) {
            quests.add(new Quest(MiscType.GOLD_BAR, 1));
            quests.add(new Quest(FoodTypes.PUMPKIN, 1));
            quests.add(new Quest(FoodTypes.WHEAT, 50));
        } else if (name.equals("Harvey")) {
            quests.add(new Quest(CropSeedsType.BLUE_JAZZ, 12));
            quests.add(new Quest(FishType.SALMON, 1));
            quests.add(new Quest(FoodTypes.WINE, 1));
        } else if (name.equals("Lia")) {
            quests.add(new Quest(MiscType.WOOD, 10));
            quests.add(new Quest(FishType.SALMON, 1));
            quests.add(new Quest(MiscType.WOOD, 200));
        } else {
            quests.add(new Quest(MiscType.WOOD, 80));
            quests.add(new Quest(MiscType.IRON_BAR, 10));
            quests.add(new Quest(MiscType.WOOD, 1000));
        }
    }

    public void initializeNpcFavorites(String name) {
        if (name.equals("Sebastian")) {
            favoriteItems.add(MiscType.WOOL);
            favoriteItems.add(FoodTypes.PUMPKIN_PIE);
            favoriteItems.add(FoodTypes.PIZZA);
        } else if (name.equals("Abigale")) {
            favoriteItems.add(ForagingMineralsType.STONE);
            favoriteItems.add(ForagingMineralsType.IRON_ORE);
            favoriteItems.add(FoodTypes.COFFEE);
        } else if (name.equals("Harvey")) {
            favoriteItems.add(FoodTypes.COFFEE);
            favoriteItems.add(FoodTypes.PICKLES);
            favoriteItems.add(FoodTypes.WINE);
        } else if (name.equals("Lia")) {
            favoriteItems.add(FoodTypes.SALAD);
            favoriteItems.add(FoodTypes.GRAPE);
            favoriteItems.add(FoodTypes.WINE);
        } else {
            favoriteItems.add(FoodTypes.SPAGHETTI);
            favoriteItems.add(MiscType.WOOD);
            favoriteItems.add(MiscType.IRON_BAR);
        }
    }

    public void initializeNpcRewards(String name) {
        if (name.equals("Sebastian")) {
           rewards.add(new NPCReward(0,ForagingMineralsType.DIAMOND,0,3));
           rewards.add(new NPCReward(5000,null,0,0));
           rewards.add(new NPCReward(0,ForagingMineralsType.QUARTZ,0,50));
        } else if (name.equals("Abigale")) {
            rewards.add(new NPCReward(0,null,1,0));
            rewards.add(new NPCReward(500,null,0,0));
            rewards.add(new NPCReward(0,MiscType.IRIDIUM_SPRINKLER,0,1));
        } else if (name.equals("Harvey")) {
            rewards.add(new NPCReward(750,null,0,0));
            rewards.add(new NPCReward(0,null,1,0));
            rewards.add(new NPCReward(0,FoodTypes.SALAD,0,5));
        } else if (name.equals("Lia")) {
            rewards.add(new NPCReward(500,null,0,0));
            rewards.add(new NPCReward(0,FoodTypes.SALMON_DINNER,0,1));
            rewards.add(new NPCReward(0,MiscType.DELUXE_SCARE_CROW,0,3));
        } else {
            rewards.add(new NPCReward(1000,null,0,0));
            rewards.add(new NPCReward(0,MiscType.BEE_HOUSE,0,3));
            rewards.add(new NPCReward(25000,MiscType.BEE_HOUSE,0,0));
        }
    }

    public HashMap<String, Boolean> getHasTalked() {
        return hasTalked;
    }

    public void setHasTalked(HashMap<String, Boolean> hasTalked) {
        this.hasTalked = hasTalked;
    }

    public HashMap<String, Boolean> getGift() {
        return gift;
    }

    public void setGift(HashMap<String, Boolean> gift) {
        this.gift = gift;
    }

    public boolean isItemInFavorite(Item item) {
        ItemType type = null;
        if (item instanceof Fish) {
            type = ((Fish) item).getFishType();
        } else if (item instanceof Food) {
            type = ((Food) item).foodTypes;
        } else if (item instanceof ForagingMineral) {
            type = ((ForagingMineral) item).getType();
        } else if (item instanceof Misc) {
            type = ((Misc) item).getMiscType();
        } else if (item instanceof Seed) {
            type = ((Seed) item).getCropType();
        } else if (item instanceof TreeSeed) {
            type = ((TreeSeed) item).getTreeSeedsType();
        }
        if (favoriteItems.contains(type)) {
            return true;
        }
        return false;
    }

    public String questsToString(int level) {
        StringBuilder sb = new StringBuilder();
        sb.append("Quests : \n");
        for (int i = 0; i < (level >= 1 ? quests.size() : quests.size() - 1); i++) {
            sb.append(i).append(" : ").append(quests.get(i).toString()).append("\n");
        }
        return sb.toString();
    }
}
