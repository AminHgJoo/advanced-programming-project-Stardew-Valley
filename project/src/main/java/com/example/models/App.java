package com.example.models;

import com.example.Repositories.UserRepository;
import com.example.models.enums.types.*;

import java.util.ArrayList;
import java.util.HashMap;

public class App {
    final private static ArrayList<User> users = new ArrayList<>();
    private static User loggedInUser = UserRepository.getStayLoggedInUser();
    private static MenuTypes currMenuType = MenuTypes.SignInMenu;

    private static HashMap<String, ItemType> allItemTypes = new HashMap<>();

    static {
        for (ArtisanBlockType artisanBlockType : ArtisanBlockType.values()) {
            allItemTypes.put(artisanBlockType.name, artisanBlockType);
        }
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
        for (ForagingSeedsType foragingSeedsType : ForagingSeedsType.values()) {
            allItemTypes.put(foragingSeedsType.name, foragingSeedsType);
        }
        for (ForagingTreeSeedsType foragingTreeSeedsType : ForagingTreeSeedsType.values()) {
            allItemTypes.put(foragingTreeSeedsType.name, foragingTreeSeedsType);
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
