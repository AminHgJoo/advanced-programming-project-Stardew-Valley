package com.example.models.NPCModels;

import com.example.models.Quest;
import com.example.models.enums.types.itemTypes.*;
import com.example.models.items.Food;
import com.example.models.items.ForagingMineral;
import com.example.models.items.Item;
import com.example.models.mapModels.Coordinate;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class NPC {
    private String name;
    private ArrayList<ItemType> favoriteItems = new ArrayList<>();
    private ArrayList<NPCRequest> npcRequests = new ArrayList<>();
    private ArrayList<NPCReward> rewards = new ArrayList<>();
    private Coordinate coordinate;
    private ArrayList<NPCFriendship> friendships = new ArrayList<>();
    private ArrayList<NPCDialogue> dialogues = new ArrayList<>();
    private ArrayList<Quest> quests = new ArrayList<>();

    public NPC() {
    }

    public NPC(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
        initializeNpcQuest(name);
        initializeNpcFavorites(name);
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

    public ArrayList<NPCDialogue> getDialogues() {
        return dialogues;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public void initializeNpcQuest(String name){
        if(name.equals("Sebastian")){
            quests.add(new Quest(ForagingMineralsType.IRON_ORE , 50));
            quests.add(new Quest(FoodTypes.PUMPKIN_PIE, 1));
            quests.add(new Quest(ForagingMineralsType.STONE , 150));
        }else if(name.equals("Abigale")){
            quests.add(new Quest(MiscType.GOLD_BAR, 1));
            quests.add(new Quest(FoodTypes.PUMPKIN , 1));
            quests.add(new Quest(FoodTypes.WHEAT , 50));
        }else if(name.equals("Harvey")){
            quests.add(new Quest(CropSeedsType.BLUE_JAZZ, 12));
            quests.add(new Quest(FishType.SALMON, 1));
            quests.add(new Quest(FoodTypes.WINE, 1));
        }else if(name.equals("Lia")){
            quests.add(new Quest(MiscType.WOOD , 10));
            quests.add(new Quest(FishType.SALMON, 1));
            quests.add(new Quest(MiscType.WOOD , 200));
        }else {
            quests.add(new Quest(MiscType.WOOD , 80));
            quests.add(new Quest(MiscType.IRON_BAR , 10));
            quests.add(new Quest(MiscType.WOOD , 1000));
        }
    }

    public void initializeNpcFavorites(String name){
        if(name.equals("Sebastian")){
            favoriteItems.add(MiscType.WOOL);
            favoriteItems.add(FoodTypes.PUMPKIN_PIE);
            favoriteItems.add(FoodTypes.PIZZA);
        }else if(name.equals("Abigale")){
            favoriteItems.add(ForagingMineralsType.STONE);
            favoriteItems.add(ForagingMineralsType.IRON_ORE);
            favoriteItems.add(FoodTypes.COFFEE);
        }else if(name.equals("Harvey")){
            favoriteItems.add(FoodTypes.COFFEE);
            favoriteItems.add(FoodTypes.PICKLES);
            favoriteItems.add(FoodTypes.WINE);
        }else if(name.equals("Lia")){
            favoriteItems.add(FoodTypes.SALAD);
            favoriteItems.add(FoodTypes.GRAPE);
            favoriteItems.add(FoodTypes.WINE);
        }else {
            favoriteItems.add(FoodTypes.SPAGHETTI);
            favoriteItems.add(MiscType.WOOD);
            favoriteItems.add(MiscType.IRON_BAR);
        }
    }
}
