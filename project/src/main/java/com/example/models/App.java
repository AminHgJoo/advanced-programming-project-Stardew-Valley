package com.example.models;

import com.example.Repositories.UserRepository;
import com.example.models.enums.types.MenuTypes;
import com.example.models.enums.types.itemTypes.*;

import java.util.ArrayList;
import java.util.HashMap;

public class App {
    final private static ArrayList<User> users = new ArrayList<>();
    private static User loggedInUser = UserRepository.getStayLoggedInUser();
    private static MenuTypes currMenuType = MenuTypes.SignInMenu;

    private static final HashMap<String, ItemType> allItemTypes = new HashMap<>();

    static {
        for (CropSeedsType cropSeedsType : CropSeedsType.values()) {
            allItemTypes.put(cropSeedsType.name, cropSeedsType);
        }
        for (FishType fishType : FishType.values()) {
            allItemTypes.put(fishType.name, fishType);
        }
        for (FoodTypes foodType : FoodTypes.values()) {
            allItemTypes.put(foodType.name, foodType);
        }
        for (ForagingMineralsType foragingMineralsType : ForagingMineralsType.values()) {
            allItemTypes.put(foragingMineralsType.name, foragingMineralsType);
        }
        for (TreeSeedsType treeSeedsType : TreeSeedsType.values()) {
            allItemTypes.put(treeSeedsType.name, treeSeedsType);
        }
        for (MiscType miscType : MiscType.values()) {
            allItemTypes.put(miscType.name, miscType);
        }
        for (ToolTypes toolType : ToolTypes.values()) {
            allItemTypes.put(toolType.name, toolType);
        }
    }

    public static MenuTypes getCurrMenuType() {
        return currMenuType;
    }

    public static void setCurrMenuType(MenuTypes currMenuType) {
        App.currMenuType = currMenuType;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        App.loggedInUser = loggedInUser;
    }

    public static HashMap<String, ItemType> getAllItemTypes() {
        return allItemTypes;
    }
}
