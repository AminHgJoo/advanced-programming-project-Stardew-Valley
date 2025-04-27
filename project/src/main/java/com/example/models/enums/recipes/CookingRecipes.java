package com.example.models.enums.recipes;

import com.example.models.Slot;
import com.example.models.enums.Quality;
import com.example.models.enums.types.itemTypes.FishType;
import com.example.models.enums.types.itemTypes.FoodTypes;
import com.example.models.enums.types.itemTypes.MiscType;
import com.example.models.items.Fish;
import com.example.models.items.Food;
import com.example.models.items.Misc;

public enum CookingRecipes {

    FRIED_EGG("Fried Egg", 0, 0, 0, 0, FoodTypes.FRIED_EGG,
            new Slot(new Misc(MiscType.EGG), 1)),
    BAKED_FISH("Baked Fish", 0, 0, 0, 0, FoodTypes.BAKED_FISH,
            new Slot[]{new Slot(new Fish(Quality.DEFAULT, FishType.SARDINE), 1), new Slot(new Fish(Quality.DEFAULT, FishType.SALMON), 1), new Slot(new Food(FoodTypes.WHEAT), 1)}),
    SALAD("Salad", 0, 0, 0, 0, FoodTypes.SALAD,
            new Slot[]{new Slot(new Food(FoodTypes.LEEK), 1), new Slot(new Food(FoodTypes.DANDELION), 1)}),
    OMELETTE("Omelette", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.OMELETTE,
            new Slot[]{new Slot(new Misc(MiscType.EGG), 1), new Slot(new Misc(MiscType.MILK), 1)}),
    PUMPKIN_PIE("Pumpkin Pie", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.PUMPKIN_PIE,
            new Slot[]{new Slot(new Food(FoodTypes.PUMPKIN), 1), new Slot(new Misc(MiscType.MILK), 1), new Slot(new Food(FoodTypes.SUGAR), 1), new Slot(new Food(FoodTypes.WHEAT_FLOUR), 1)}),
    SPAGHETTI("Spaghetti", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.SPAGHETTI,
            new Slot[]{new Slot(new Food(FoodTypes.WHEAT_FLOUR), 1), new Slot(new Food(FoodTypes.TOMATO), 1)}),
    PIZZA("Pizza", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.PIZZA,
            new Slot[]{new Slot(new Food(FoodTypes.WHEAT_FLOUR), 1), new Slot(new Food(FoodTypes.TOMATO), 1), new Slot(new Food(FoodTypes.CHEESE), 1)}),
    TORTILLA("Tortilla", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.TORTILLA,
            new Slot(new Food(FoodTypes.CORN), 1)),
    // 1 Fish handled hard coded in Controller.
    MAKI_ROLL("Maki Roll", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.MAKI_ROLL,
            new Slot[]{new Slot(new Misc(MiscType.FIBER), 1), new Slot(new Food(FoodTypes.RICE), 1)}),
    TRIPLE_SHOT_ESPRESSO("Triple Shot Espresso", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.TRIPLE_SHOT_ESPRESSO,
            new Slot(new Food(FoodTypes.COFFEE), 3)),
    COOKIE("Cookie", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.COOKIE,
            new Slot[]{new Slot(new Food(FoodTypes.WHEAT_FLOUR), 1), new Slot(new Misc(MiscType.EGG), 1), new Slot(new Food(FoodTypes.SUGAR), 1)}),
    HASH_BROWNS("Hash Browns", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.HASH_BROWNS,
            new Slot[]{new Slot(new Food(FoodTypes.POTATO), 1), new Slot(new Food(FoodTypes.OIL), 1)}),
    PANCAKES("Pancakes", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.PANCAKES,
            new Slot[]{new Slot(new Food(FoodTypes.WHEAT_FLOUR), 1), new Slot(new Misc(MiscType.EGG), 1)}),
    FRUIT_SALAD("Fruit Salad", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.FRUIT_SALAD,
            new Slot[]{new Slot(new Food(FoodTypes.BLUEBERRY), 1), new Slot(new Food(FoodTypes.MELON), 1), new Slot(new Food(FoodTypes.APRICOT), 1)}),
    RED_PLATE("Red Plate", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.RED_PLATE,
            new Slot[]{new Slot(new Food(FoodTypes.RED_CABBAGE), 1), new Slot(new Food(FoodTypes.RADISH), 1)}),
    BREAD("Bread", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.BREAD,
            new Slot(new Food(FoodTypes.WHEAT_FLOUR), 1)),
    SALMON_DINNER("Salmon Dinner", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, FoodTypes.SALMON_DINNER,
            new Slot[]{new Slot(new Fish(Quality.DEFAULT, FishType.SALMON), 1), new Slot(new Food(FoodTypes.AMARANTH), 1), new Slot(new Food(FoodTypes.KALE), 1)}),
    VEGETABLE_MEDLEY("Vegetable Medley", 0, 0, 2, 0, FoodTypes.VEGETABLE_MEDLEY,
            new Slot[]{new Slot(new Food(FoodTypes.TOMATO), 1), new Slot(new Food(FoodTypes.BEET), 1)}),
    FARMERS_LUNCH("Farmer's Lunch", 1, 0, 0, 0, FoodTypes.FARMERS_LUNCH,
            new Slot[]{new Slot(new Food(FoodTypes.OMELETTE), 1), new Slot(new Food(FoodTypes.PARSNIP), 1)}),
    SURVIVAL_BURGER("Survival Burger", 0, 0, 3, 0, FoodTypes.SURVIVAL_BURGER,
            new Slot[]{new Slot(new Food(FoodTypes.BREAD), 1), new Slot(new Food(FoodTypes.CARROT), 1), new Slot(new Food(FoodTypes.EGGPLANT), 1)}),
    DISH_OF_THE_SEA("Dish 'O The Sea", 0, 0, 0, 2, FoodTypes.DISH_OF_THE_SEA,
            new Slot[]{new Slot(new Fish(Quality.DEFAULT, FishType.SARDINE), 2), new Slot(new Food(FoodTypes.HASH_BROWNS), 1)}),
    SEAFORM_PUDDING("Seaform Pudding", 0, 0, 0, 3, FoodTypes.SEAFORM_PUDDING,
            new Slot[]{new Slot(new Fish(Quality.DEFAULT, FishType.FLOUNDER), 1), new Slot(new Fish(Quality.DEFAULT, FishType.MIDNIGHT_CARP), 1)}),
    MINERS_TREAT("Miner's Treat", 0, 1, 0, 0, FoodTypes.MINERS_TREAT,
            new Slot[]{new Slot(new Misc(MiscType.MILK), 1), new Slot(new Food(FoodTypes.SUGAR), 1), new Slot(new Food(FoodTypes.CARROT), 2)});


    public final String name;
    public final int farmingLevel;
    public final int miningLevel;
    public final int foragingLevel;
    public final int fishingLevel;
    public final FoodTypes craftingResultType;
    public final Slot[] ingredients;

    CookingRecipes(String name, int farmingLevel, int miningLevel, int foragingLevel, int fishingLevel
            , FoodTypes craftingResultType, Slot[] ingredients) {
        this.name = name;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.fishingLevel = fishingLevel;
        this.craftingResultType = craftingResultType;
        this.ingredients = ingredients;
    }

    /// For single ingredient crafting recipes.
    CookingRecipes(String name, int farmingLevel, int miningLevel, int foragingLevel, int fishingLevel
            , FoodTypes craftingResultType, Slot ingredient) {
        this.name = name;
        this.farmingLevel = farmingLevel;
        this.miningLevel = miningLevel;
        this.foragingLevel = foragingLevel;
        this.fishingLevel = fishingLevel;
        this.craftingResultType = craftingResultType;
        this.ingredients = new Slot[]{ingredient};
    }

    @Override
    public String toString() {
        return name;
    }
}
