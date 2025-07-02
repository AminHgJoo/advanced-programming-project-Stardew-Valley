package com.common.models.enums.types.storeProductTypes;

import com.common.models.enums.types.itemTypes.*;
import com.common.models.enums.worldEnums.Season;

public enum AllProducts {
    COPPER_ORE("Copper Ore", "A common ore that can be smelted into bars.", ForagingMineralsType.COPPER_ORE, 75, 75, Double.POSITIVE_INFINITY, Season.values()),
    IRON_ORE("Iron Ore", "A fairly common ore that can be smelted into bars.", ForagingMineralsType.IRON_ORE, 150, 150, Double.POSITIVE_INFINITY, Season.values()),
    COAL("Coal", "A combustible rock that is useful for crafting and smelting.", ForagingMineralsType.COAL, 150, 150, Double.POSITIVE_INFINITY, Season.values()),
    GOLD_ORE("Gold Ore", "A precious ore that can be smelted into bars.", ForagingMineralsType.GOLD_ORE, 400, 400, Double.POSITIVE_INFINITY, Season.values()),
    COPPER_TOOL("Copper Tool", MiscType.COPPER_BAR, 2000, 2000, 1, Season.values()),
    STEEL_TOOL("Steel Tool", MiscType.IRON_BAR, 5000, 5000, 1, Season.values()),
    GOLD_TOOL("Gold Tool", MiscType.GOLD_BAR, 10000, 10000, 1, Season.values()),
    IRIDIUM_TOOL("Iridium Tool", MiscType.IRIDIUM_BAR, 25000, 25000, 1, Season.values()),
    COPPER_TRASH_CAN("Copper Trash Can", MiscType.COPPER_BAR, 1000, 1000, 1, Season.values()),
    STEEL_TRASH_CAN("Steel Trash Can", MiscType.COPPER_BAR, 2500, 2500, 1, Season.values()),
    GOLD_TRASH_CAN("Gold Trash Can", MiscType.COPPER_BAR, 5000, 5000, 1, Season.values()),
    IRIDIUM_TRASH_CAN("Iridium Trash Can", MiscType.COPPER_BAR, 12500, 12500, 1, Season.values()),
    WOOD("Wood", "A sturdy, yet flexible plant material with a wide variety of uses.", MiscType.WOOD, 10, 10, Double.POSITIVE_INFINITY, Season.values()),
    STONE("Stone", "A common material with many uses in crafting and building.", ForagingMineralsType.STONE, 20, 20, Double.POSITIVE_INFINITY, Season.values()),
    BARN("Barn", "Houses 4 barn-dwelling animals.", ForagingMineralsType.STONE, 20, 20, 1, Season.values()),
    BIG_BARN("Big Barn", "Houses 8 barn-dwelling animals. Unlocks goats.", ForagingMineralsType.STONE, 20, 20, 1, Season.values()),
    DELUXE_BARN("Deluxe Barn", "Houses 12 barn-dwelling animals. Unlocks sheep and pigs.", ForagingMineralsType.STONE, 20, 20, 1, Season.values()),
    COOP("Coop", "Houses 4 coop-dwelling animals.", ForagingMineralsType.STONE, 20, 20, 1, Season.values()),
    BIG_COOP("Big Coop", "Houses 8 coop-dwelling animals. Unlocks ducks.", ForagingMineralsType.STONE, 20, 20, 1, Season.values()),
    DELUXE_COOP("Deluxe Coop", "Houses 12 coop-dwelling animals. Unlocks rabbits.", ForagingMineralsType.STONE, 20, 20, 1, Season.values()),
    WELL("Well", "Provides a place for you to refill your watering can.", ForagingMineralsType.STONE, 20, 20, 1, Season.values()),
    SHIPPING_BIN("Shipping Bin", "Items placed in it will be included in the nightly shipment.", ForagingMineralsType.STONE, 20, 20, Double.POSITIVE_INFINITY, Season.values()),
    TROUT_SOUP("Trout Soup", "Pretty salty.", FoodTypes.TROUT_SOUP, 250, 250, 1, null, Season.values()),
    BAMBOO_POLE("Bamboo Pole", "Use in the water to catch fish.", ToolTypes.FISHING_ROD, 500, 500, 1, null, Season.values()),
    TRAINING_ROD("Training Rod", "It's a lot easier to use than other rods, but can only catch basic fish.", ToolTypes.FISHING_ROD, 25, 25, 1, null, Season.values()),
    FIBERGLASS_ROD("Fiberglass Rod", "Use in the water to catch fish.", ToolTypes.FISHING_ROD, 1800, 1800, 1, 2, Season.values()),
    IRIDIUM_ROD("Iridium Rod", "Use in the water to catch fish.", ToolTypes.FISHING_ROD, 7500, 7500, 1, 4, Season.values()),
    JOJA_COLA("Joja Cola", "The flagship product of Joja corporation.", FoodTypes.JOJA_COLA, 75, 75, Double.POSITIVE_INFINITY, Season.values()),
    ANCIENT_SEED("Ancient Seed", "Could this still grow?", CropSeedsType.ANCIENT_FRUIT, 500, 500, 1, Season.values()),
    GRASS_STARTER("Grass Starter", "Place this on your farm to start a new patch of grass.", MiscType.GRASS_STARTER, 125, 125, Double.POSITIVE_INFINITY, Season.values()),
    SUGAR("Sugar", "Adds sweetness to pastries and candies. Too much can be unhealthy.", FoodTypes.SUGAR, 125, 125, Double.POSITIVE_INFINITY, Season.values()),
    WHEAT_FLOUR("Wheat Flour", "A common cooking ingredient made from crushed wheat seeds.", FoodTypes.WHEAT_FLOUR, 125, 125, Double.POSITIVE_INFINITY, Season.values()),
    RICE("Rice", "A basic grain often served under vegetables.", FoodTypes.RICE, 250, 250, Double.POSITIVE_INFINITY, Season.values()),
    PARSNIP_SEEDS("Parsnip Seeds", "Plant these in the spring. Takes 4 days to mature.", CropSeedsType.PARSNIP, 25, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SPRING}),
    BEAN_STARTER("Bean Starter", "Plant these in the spring. Takes 10 days to mature, but keeps producing after that. Grows on a trellis.", CropSeedsType.GREEN_BEAN, 75, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SPRING}),
    CAULIFLOWER_SEEDS("Cauliflower Seeds", "Plant these in the spring. Takes 12 days to produce a large cauliflower.", CropSeedsType.CAULIFLOWER, 100, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SPRING}),
    POTATO_SEEDS("Potato Seeds", "Plant these in the spring. Takes 6 days to mature, and has a chance of yielding multiple potatoes at harvest.", CropSeedsType.POTATO, 62, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SPRING}),
    STRAWBERRY_SEEDS("Strawberry Seeds", "Plant these in spring. Takes 8 days to mature, and keeps producing strawberries after that.", CropSeedsType.STRAWBERRY, 100, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SPRING}),
    TULIP_BULB("Tulip Bulb", "Plant in spring. Takes 6 days to produce a colorful flower. Assorted colors.", null, 25, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SPRING}),
    KALE_SEEDS("Kale Seeds", "Plant these in the spring. Takes 6 days to mature. Harvest with the scythe.", CropSeedsType.KALE, 87, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SPRING}),
    COFFEE_BEANS("Coffee Beans", "Plant in summer or spring. Takes 10 days to grow, Then produces coffee Beans every other day.", CropSeedsType.COFFEE_BEAN, 200, Double.POSITIVE_INFINITY, 1, new Season[]{Season.SPRING, Season.SUMMER}),
    CARROT_SEEDS("Carrot Seeds", "Plant in the spring. Takes 3 days to grow.", CropSeedsType.CARROT, 5, Double.POSITIVE_INFINITY, 10, new Season[]{Season.SPRING}),
    RHUBARB_SEEDS("Rhubarb Seeds", "Plant these in the spring. Takes 13 days to mature.", CropSeedsType.RHUBARB, 100, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SPRING}),
    JAZZ_SEEDS("Jazz Seeds", "Plant in spring. Takes 7 days to produce a blue puffball flower.", CropSeedsType.BLUE_JAZZ, 37, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SPRING}),
    TOMATO_SEEDS("Tomato Seeds", "Plant these in the summer. Takes 11 days to mature, and continues to produce after first harvest.", CropSeedsType.TOMATO, 62, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    PEPPER_SEEDS("Pepper Seeds", "Plant these in the summer. Takes 5 days to mature, and continues to produce after first harvest.", CropSeedsType.HOT_PEPPER, 50, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    WHEAT_SEEDS("Wheat Seeds", "Plant these in the summer or fall. Takes 4 days to mature. Harvest with the scythe.", CropSeedsType.WHEAT, 12, Double.POSITIVE_INFINITY, 10, new Season[]{Season.SUMMER, Season.FALL}),
    SUMMER_SQUASH_SEEDS("Summer Squash Seeds", "Plant in the summer. Takes 6 days to grow, and continues to produce after first harvest.", CropSeedsType.SUMMER_SQUASH, 10, Double.POSITIVE_INFINITY, 10, new Season[]{Season.SUMMER}),
    RADISH_SEEDS("Radish Seeds", "Plant these in the summer. Takes 6 days to mature.", CropSeedsType.RADISH, 50, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    MELON_SEEDS("Melon Seeds", "Plant these in the summer. Takes 12 days to mature.", CropSeedsType.MELON, 100, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    HOPS_STARTER("Hops Starter", "Plant these in the summer. Takes 11 days to grow, but keeps producing after that. Grows on a trellis.", CropSeedsType.HOPS, 75, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    POPPY_SEEDS("Poppy Seeds", "Plant in summer. Produces a bright red flower in 7 days.", CropSeedsType.POPPY, 125, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    SPANGLE_SEEDS("Spangle Seeds", "Plant in summer. Takes 8 days to produce a vibrant tropical flower. Assorted colors.", CropSeedsType.SUMMER_SPANGLE, 62, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    STARFRUIT_SEEDS("Starfruit Seeds", "Plant these in the summer. Takes 13 days to mature.", CropSeedsType.STARFRUIT, 400, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER}),
    SUNFLOWER_SEEDS("Sunflower Seeds", "Plant in summer or fall. Takes 8 days to produce a large sunflower. Yields more seeds at harvest.", CropSeedsType.SUNFLOWER, 125, Double.POSITIVE_INFINITY, 5, new Season[]{Season.SUMMER, Season.FALL}),
    CORN_SEEDS("Corn Seeds", "Plant these in the summer or fall. Takes 14 days to mature, and continues to produce after first harvest.", CropSeedsType.CORN, 187, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    EGGPLANT_SEEDS("Eggplant Seeds", "Plant these in the fall. Takes 5 days to mature, and continues to produce after first harvest.", CropSeedsType.EGGPLANT, 25, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    PUMPKIN_SEEDS("Pumpkin Seeds", "Plant these in the fall. Takes 13 days to mature.", CropSeedsType.PUMPKIN, 125, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    BROCCOLI_SEEDS("Broccoli Seeds", "Plant in the fall. Takes 8 days to mature, and continues to produce after first harvest.", CropSeedsType.BROCCOLI, 15, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    AMARANTH_SEEDS("Amaranth Seeds", "Plant these in the fall. Takes 7 days to grow. Harvest with the scythe.", CropSeedsType.AMARANTH, 87, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    GRAPE_STARTER("Grape Starter", "Plant these in the fall. Takes 10 days to grow, but keeps producing after that. Grows on a trellis.", CropSeedsType.GRAPE, 75, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    BEET_SEEDS("Beet Seeds", "Plant these in the fall. Takes 6 days to mature.", CropSeedsType.BEET, 20, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    YAM_SEEDS("Yam Seeds", "Plant these in the fall. Takes 10 days to mature.", CropSeedsType.YAM, 75, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    BOK_CHOY_SEEDS("Bok Choy Seeds", "Plant these in the fall. Takes 4 days to mature.", CropSeedsType.BOK_CHOY, 62, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    CRANBERRY_SEEDS("Cranberry Seeds", "Plant these in the fall. Takes 7 days to mature, and continues to produce after first harvest.", CropSeedsType.CRANBERRIES, 300, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    FAIRY_SEEDS("Fairy Seeds", "Plant in fall. Takes 12 days to produce a mysterious flower. Assorted Colors.", CropSeedsType.FAIRY_ROSE, 250, Double.POSITIVE_INFINITY, 5, new Season[]{Season.FALL}),
    RARE_SEED("Rare Seed", "Sow in fall. Takes all season to grow.", CropSeedsType.SWEET_GEM_BERRY, 1000, Double.POSITIVE_INFINITY, 1, new Season[]{Season.FALL}),
    POWDERMELON_SEEDS("Powdermelon Seeds", "This special melon grows in the winter. Takes 7 days to grow.", CropSeedsType.POWDER_MELON, 20, Double.POSITIVE_INFINITY, 10, new Season[]{Season.WINTER}),
    HAY("Hay", "Dried grass used as animal food.", MiscType.WOOD, 50, 50, Double.POSITIVE_INFINITY, Season.values()),
    MILK_PAIL("Milk Pail", "Gather milk from your animals.", ForagingMineralsType.STONE, 1000, 1000, 1, Season.values()),
    SHEARS("Shears", "Use this to collect wool from sheep", ForagingMineralsType.STONE, 1000, 1000, 1, Season.values()),
    CHICKEN("Chicken", "Well cared-for chickens lay eggs every day. Lives in the coop.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    COW("Cow", "Can be milked daily. A milk pail is required to harvest the milk. Lives in the barn.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    GOAT("Goat", "Happy provide goat milk every other day. A milk pail is required to harvest the milk. Lives in the barn.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    DUCK("Duck", "Happy lay duck eggs every other day. Lives in the coop.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    SHEEP("Sheep", "Can be shorn for wool. A pair of shears is required to harvest the wool. Lives in the barn.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    RABBIT("Rabbit", "Provides a place for you to refill your watering can.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    DINOSAUR("Dinosaur", "The Dinosaur is a farm animal that lives in a Big Coop", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    PIG("Pig", "These pigs are trained to find truffles! Lives in the barn.", ForagingMineralsType.STONE, 20, 20, 2, Season.values()),
    DEHYDRATOR("Dehydrator", "A recipe to make Dehydrator", null, 10000, 10000, 1, Season.values()),
    GRASS_STARTER_RECIPE("Grass Starter Recipe", "A recipe to make Grass Starter", null, 1000, 1000, 1, Season.values()),
    RICE_P("Pierre Rice", "A basic grain often served under vegetables.", FoodTypes.RICE, 200, 200, Double.POSITIVE_INFINITY, Season.values()),
    WHEAT_FLOUR_P("Pierre Wheat Flour", "A common cooking ingredient made from crushed wheat seeds.", FoodTypes.WHEAT_FLOUR, 100, 100, Double.POSITIVE_INFINITY, Season.values()),
    BOUQUET("Bouquet", "A gift that shows your romantic interest.\n(Unlocked after reaching level 2 friendship with a player)", MiscType.BOUQUET, 1000, 1000, 2, Season.values()),
    WEDDING_RING("Wedding Ring", "It's used to ask for another farmer's hand in marriage.\n(Unlocked after reaching level 3 friendship with a player)", MiscType.WEDDING_RING, 10000, 10000, 2, Season.values()),
    SUGAR_P("Sugar", "Adds sweetness to pastries and candies. Too much can be unhealthy.", FoodTypes.SUGAR, 100, 100, Double.POSITIVE_INFINITY, Season.values()),
    OIL("Oil", "All purpose cooking oil.", FoodTypes.OIL, 200, 200, Double.POSITIVE_INFINITY, Season.values()),
    VINEGAR("Vinegar", "An aged fermented liquid used in many cooking recipes.", FoodTypes.VINEGAR, 200, 200, Double.POSITIVE_INFINITY, Season.values()),
    DELUXE_RETAINING_SOIL("Deluxe Retaining Soil", "This soil has a 100% chance of staying watered overnight. Mix into tilled soil.", MiscType.DELUXE_RETAINING_SOIL, 150, 150, Double.POSITIVE_INFINITY, Season.values()),
    GRASS_STARTER_P("Grass Starter", "Place this on your farm to start a new patch of grass.", MiscType.GRASS_STARTER, 100, 100, Double.POSITIVE_INFINITY, Season.values()),
    SPEED_GRO("Speed-Gro", "Makes the plants grow 1 day earlier.", MiscType.SPEED_GRO, 100, 100, Double.POSITIVE_INFINITY, Season.values()),
    APPLE_SAPLING("Apple Sapling", "Takes 28 days to produce a mature Apple tree. Bears fruit in the fall. Only grows if the 8 surrounding \"tiles\" are empty.", TreeSeedsType.APPLE_SAPLING, 4000, 4000, Double.POSITIVE_INFINITY, Season.values()),
    APRICOT_SAPLING("Apricot Sapling", "Takes 28 days to produce a mature Apricot tree. Bears fruit in the spring. Only grows if the 8 surrounding \"tiles\" are empty.", TreeSeedsType.APRICOT_SAPLING, 2000, 2000, Double.POSITIVE_INFINITY, Season.values()),
    CHERRY_SAPLING("Cherry Sapling", "Takes 28 days to produce a mature Cherry tree. Bears fruit in the spring. Only grows if the 8 surrounding \"tiles\" are empty.", TreeSeedsType.CHERRY_SAPLING, 3400, 3400, Double.POSITIVE_INFINITY, Season.values()),
    ORANGE_SAPLING("Orange Sapling", "Takes 28 days to produce a mature Orange tree. Bears fruit in the summer. Only grows if the 8 surrounding \"tiles\" are empty.", TreeSeedsType.ORANGE_SAPLING, 4000, 4000, Double.POSITIVE_INFINITY, Season.values()),
    PEACH_SAPLING("Peach Sapling", "Takes 28 days to produce a mature Peach tree. Bears fruit in the summer. Only grows if the 8 surrounding \"tiles\" are empty.", TreeSeedsType.PEACH_SAPLING, 6000, 6000, Double.POSITIVE_INFINITY, Season.values()),
    POMEGRANATE_SAPLING("Pomegranate Sapling", "Takes 28 days to produce a mature Pomegranate tree. Bears fruit in the fall. Only grows if the 8 surrounding \"tiles\" are empty.", TreeSeedsType.POMEGRANATE_SAPLING, 6000, 6000, Double.POSITIVE_INFINITY, Season.values()),
    BASIC_RETAINING_SOIL("Basic Retaining Soil", "This soil has a chance of staying watered overnight. Mix into tilled soil.", MiscType.BASIC_RETAINING_SOIL, 100, 100, Double.POSITIVE_INFINITY, Season.values()),
    QUALITY_RETAINING_SOIL("Quality Retaining Soil", "This soil has a good chance of staying watered overnight. Mix into tilled soil.", MiscType.QUALITY_RETAINING_SOIL, 150, 150, Double.POSITIVE_INFINITY, Season.values()),
    PARSNIP_SEEDS_P("Pierre Parsnip Seeds", "Plant these in the spring. Takes 4 days to mature.", CropSeedsType.PARSNIP, 20, 30, 5, new Season[]{Season.SPRING}),
    BEAN_STARTER_P("Pierre Bean Starter", "Plant these in the spring. Takes 10 days to mature, but keeps producing after that. Grows on a trellis.", CropSeedsType.GREEN_BEAN, 60, 90, 5, new Season[]{Season.SPRING}),
    CAULIFLOWER_SEEDS_P("Pierre Cauliflower Seeds", "Plant these in the spring. Takes 12 days to produce a large cauliflower.", CropSeedsType.CAULIFLOWER, 80, 120, 5, new Season[]{Season.SPRING}),
    POTATO_SEEDS_P("Pierre Potato Seeds", "Plant these in the spring. Takes 6 days to mature, and has a chance of yielding multiple potatoes at harvest.", CropSeedsType.POTATO, 50, 75, 5, new Season[]{Season.SPRING}),
    TULIP_BULB_P("Pierre Tulip Bulb", "Plant in spring. Takes 6 days to produce a colorful flower. Assorted colors.", CropSeedsType.TULIP, 20, 30, 5, new Season[]{Season.SPRING}),
    KALE_SEEDS_P("Pierre Kale Seeds", "Plant these in the spring. Takes 6 days to mature. Harvest with the scythe.", CropSeedsType.KALE, 70, 105, 5, new Season[]{Season.SPRING}),
    JAZZ_SEEDS_P("Pierre Jazz Seeds", "Plant in spring. Takes 7 days to produce a blue puffball flower.", CropSeedsType.BLUE_JAZZ, 30, 45, 5, new Season[]{Season.SPRING}),
    GARLIC_SEEDS("Garlic Seeds", "Plant these in the spring. Takes 4 days to mature.", CropSeedsType.GARLIC, 40, 60, 5, new Season[]{Season.SPRING}),
    RICE_SHOOT("Rice Shoot", "Plant these in the spring. Takes 8 days to mature. Grows faster if planted near a body of water.\nHarvest with the scythe.", CropSeedsType.UNMILLED_RICE, 40, 60, 5, new Season[]{Season.SPRING}),
    MELON_SEEDS_P("Pierre Melon Seeds", "Plant these in the summer. Takes 12 days to mature.", CropSeedsType.MELON, 80, 120, 5, new Season[]{Season.SUMMER}),
    TOMATO_SEEDS_P("Pierre Tomato Seeds", "Plant these in the summer. Takes 11 days to mature, and continues to produce after first harvest.", CropSeedsType.TOMATO, 50, 75, 5, new Season[]{Season.SUMMER}),
    BLUEBERRY_SEEDS("Blueberry Seeds", "Plant these in the summer. Takes 13 days to mature, and continues to produce after first harvest.", CropSeedsType.BLUEBERRY, 80, 120, 5, new Season[]{Season.SUMMER}),
    PEPPER_SEEDS_P("Pierre Pepper Seeds", "Plant these in the summer. Takes 5 days to mature, and continues to produce after first harvest.", CropSeedsType.HOT_PEPPER, 40, 60, 5, new Season[]{Season.SUMMER}),
    WHEAT_SEEDS_P("Pierre Wheat Seeds", "Plant these in the summer or fall. Takes 4 days to mature. Harvest with the scythe.", CropSeedsType.WHEAT, 10, 15, 5, new Season[]{Season.SUMMER, Season.FALL}),
    RADISH_SEEDS_P("Pierre Radish Seeds", "Plant these in the summer. Takes 6 days to mature.", CropSeedsType.RADISH, 40, 60, 5, new Season[]{Season.SUMMER}),
    POPPY_SEEDS_P("Pierre Poppy Seeds", "Plant in summer. Produces a bright red flower in 7 days.", CropSeedsType.POPPY, 100, 150, 5, new Season[]{Season.SUMMER}),
    SPANGLE_SEEDS_P("Pierre Spangle Seeds", "Plant in summer. Takes 8 days to produce a vibrant tropical flower. Assorted colors.", CropSeedsType.SUMMER_SPANGLE, 50, 75, 5, new Season[]{Season.SUMMER}),
    HOPS_STARTER_P("Pierre Hops Starter", "Plant these in the summer. Takes 11 days to grow, but keeps producing after that. Grows on a trellis.", CropSeedsType.HOPS, 60, 90, 5, new Season[]{Season.SUMMER}),
    CORN_SEEDS_P("Pierre Corn Seeds", "Plant these in the summer or fall. Takes 14 days to mature, and continues to produce after first harvest.", CropSeedsType.CORN, 150, 225, 5, new Season[]{Season.SUMMER, Season.FALL}),
    SUNFLOWER_SEEDS_P("Pierre Sunflower Seeds", "Plant in summer or fall. Takes 8 days to produce a large sunflower. Yields more seeds at harvest.", CropSeedsType.SUNFLOWER, 200, 300, 5, new Season[]{Season.SUMMER, Season.FALL}),
    RED_CABBAGE_SEEDS("Red Cabbage Seeds", "Plant these in the summer. Takes 9 days to mature.", CropSeedsType.RED_CABBAGE, 100, 150, 5, new Season[]{Season.SUMMER}),
    EGGPLANT_SEEDS_P("Pierre Eggplant Seeds", "Plant these in the fall. Takes 5 days to mature, and continues to produce after first harvest.", CropSeedsType.EGGPLANT, 20, 30, 5, new Season[]{Season.FALL}),
    PUMPKIN_SEEDS_P("Pierre Pumpkin Seeds", "Plant these in the fall. Takes 13 days to mature.", CropSeedsType.PUMPKIN, 100, 150, 5, new Season[]{Season.FALL}),
    BOK_CHOY_SEEDS_P("Pierre Bok Choy Seeds", "Plant these in the fall. Takes 4 days to mature.", CropSeedsType.BOK_CHOY, 50, 75, 5, new Season[]{Season.FALL}),
    YAM_SEEDS_P("Pierre Yam Seeds", "Plant these in the fall. Takes 10 days to mature.", CropSeedsType.YAM, 60, 90, 5, new Season[]{Season.FALL}),
    CRANBERRY_SEEDS_P("Pierre Cranberry Seeds", "Plant these in the fall. Takes 7 days to mature, and continues to produce after first harvest.", CropSeedsType.CRANBERRIES, 240, 360, 5, new Season[]{Season.FALL}),
    FAIRY_SEEDS_P("Pierre Fairy Seeds", "Plant in fall. Takes 12 days to produce a mysterious flower. Assorted Colors.", CropSeedsType.FAIRY_ROSE, 200, 300, 5, new Season[]{Season.FALL}),
    AMARANTH_SEEDS_P("Pierre Amaranth Seeds", "Plant these in the fall. Takes 7 days to grow. Harvest with the scythe.", CropSeedsType.AMARANTH, 70, 105, 5, new Season[]{Season.FALL}),
    GRAPE_STARTER_P("Pierre Grape Starter", "Plant these in the fall. Takes 10 days to grow, but keeps producing after that. Grows on a trellis.", CropSeedsType.GRAPE, 60, 90, 5, new Season[]{Season.FALL}),
    ARTICHOKE_SEEDS("Artichoke Seeds", "Plant these in the fall. Takes 8 days to mature.", CropSeedsType.ARTICHOKE, 30, 45, 5, new Season[]{Season.FALL}),
    HASHBROWNS("Hashbrowns", "A recipe to make Hashbrowns", null, 50, 50, 1, Season.values()),
    OMELET("Omelette", "A recipe to make Omelet", null, 100, 100, 1, Season.values()),
    PANCAKES("Pancakes", "A recipe to make Pancakes", null, 100, 100, 1, Season.values()),
    BREAD_RECIPE("Bread Recipe", "A recipe to make Bread", null, 100, 100, 1, Season.values()),
    TORTILLA("Tortilla", "A recipe to make Tortilla", null, 100, 100, 1, Season.values()),
    PIZZA_RECIPE("Pizza Recipe", "A recipe to make Pizza", null, 150, 150, 1, Season.values()),
    MAKI_ROLL("Maki Roll", "A recipe to make Maki Roll", null, 300, 300, 1, Season.values()),
    TRIPLE_SHOT_ESPRESSO("Triple Shot Espresso", "A recipe to make Triple Shot Espresso", null, 5000, 5000, 1, Season.values()),
    COOKIE("Cookie", "A recipe to make Cookie", null, 300, 300, 1, Season.values()),
    BEER("Beer", "Drink in moderation.", FoodTypes.BEER, 400, 400, Double.POSITIVE_INFINITY, Season.values()),
    SALAD("Salad", "A healthy garden salad.", FoodTypes.SALAD, 220, 220, Double.POSITIVE_INFINITY, Season.values()),
    BREAD("Bread", "A crusty baguette.", FoodTypes.BREAD, 120, 120, Double.POSITIVE_INFINITY, Season.values()),
    SPAGHETTI("Spaghetti", "An old favorite.", FoodTypes.SPAGHETTI, 240, 240, Double.POSITIVE_INFINITY, Season.values()),
    PIZZA("Pizza", "It's popular for all the right reasons.", FoodTypes.PIZZA, 600, 600, Double.POSITIVE_INFINITY, Season.values()),
    COFFEE("Coffee", "It smells delicious. This is sure to give you a boost.", FoodTypes.COFFEE, 300, 300, Double.POSITIVE_INFINITY, Season.values()),
    WOOD_S("Stardrop Wood", "A sturdy, yet flexible plant material with a wide variety of uses.", MiscType.WOOD, 10, 10, Double.POSITIVE_INFINITY, Season.values()),
    STONE_S("Stardrop Stone", "A common material with many uses in crafting and building.", ForagingMineralsType.STONE, 20, 20, Double.POSITIVE_INFINITY, Season.values()),
    LARGE_PACK("Large Pack", "Unlocks the 2nd row of inventory (12 more slots, total 24).", null, 2000, 2000, 1, Season.values()),
    DELUXE_PACK("Deluxe Pack", "Unlocks the 3rd row of inventory (infinite slots).", null, 10000, 10000, 1, Season.values());

    private final String name;
    private final int price;
    private final double outOfSeasonPrice;
    private final double dailyLimit;
    private String description = "";
    private String fieldName;
    private String enumName;
    private ItemType ingredient = null;
    private Integer fishingSkill;
    private Season[] seasons;

    AllProducts(String name, String description, ItemType itemType, int price, double outOfSeasonPrice, double dailyLimit, Season[] seasons) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.outOfSeasonPrice = outOfSeasonPrice;
        this.dailyLimit = dailyLimit;
        this.seasons = seasons;
        this.fieldName = itemType == null ? null : itemType.name();
        if (itemType instanceof CropSeedsType) {
            enumName = "CropSeedsType";
        } else if (itemType instanceof MiscType) {
            enumName = "MiscType";
        } else if (itemType instanceof FoodTypes) {
            enumName = "FoodTypes";
        } else if (itemType instanceof ForagingMineralsType) {
            enumName = "ForagingMineralsType";
        } else if (itemType instanceof FishType) {
            enumName = "FishType";
        } else if (itemType instanceof ToolTypes) {
            enumName = "ToolTypes";
        } else if (itemType instanceof TreeSeedsType) {
            enumName = "TreeSeedsType";
        }
    }

    AllProducts(String name, String description, ItemType itemType, int price, double outOfSeasonPrice, double dailyLimit, Integer fishingSkill, Season[] seasons) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.outOfSeasonPrice = outOfSeasonPrice;
        this.dailyLimit = dailyLimit;
        this.fishingSkill = fishingSkill;
        this.seasons = seasons;
        this.fieldName = itemType.name();
        if (itemType instanceof CropSeedsType) {
            enumName = "CropSeedsType";
        } else if (itemType instanceof MiscType) {
            enumName = "MiscType";
        } else if (itemType instanceof FoodTypes) {
            enumName = "FoodTypes";
        } else if (itemType instanceof ForagingMineralsType) {
            enumName = "ForagingMineralsType";
        } else if (itemType instanceof FishType) {
            enumName = "FishType";
        } else if (itemType instanceof ToolTypes) {
            enumName = "ToolTypes";
        } else if (itemType instanceof TreeSeedsType) {
            enumName = "TreeSeedsType";
        }
    }

    AllProducts(String name, ItemType ingredient, int price, double outOfSeasonPrice, double dailyLimit, Season[] seasons) {
        this.name = name;
        this.ingredient = ingredient;
        this.price = price;
        this.outOfSeasonPrice = outOfSeasonPrice;
        this.dailyLimit = dailyLimit;
        this.seasons = seasons;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public double getOutOfSeasonPrice() {
        return outOfSeasonPrice;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    public Season[] getSeasons() {
        return seasons;
    }

    public String getName() {
        return name;
    }

    public ItemType getItemType() {
        if (fieldName == null) {
            return null;
        } else if (enumName.equals("CropSeedsType")) {
            return CropSeedsType.valueOf(fieldName);
        } else if (enumName.equals("MiscType")) {
            return MiscType.valueOf(fieldName);
        } else if (enumName.equals("FoodTypes")) {
            return FoodTypes.valueOf(fieldName);
        } else if (enumName.equals("ForagingMineralsType")) {
            return ForagingMineralsType.valueOf(fieldName);
        } else if (enumName.equals("FishType")) {
            return FishType.valueOf(fieldName);
        } else if (enumName.equals("ToolTypes")) {
            return ToolTypes.valueOf(fieldName);
        } else if (enumName.equals("TreeSeedsType")) {
            return TreeSeedsType.valueOf(fieldName);
        }
        return null;
    }

    public boolean isInSeason(Season season) {
        Season[] seasons = getSeasons();
        boolean inSeason = false;
        for (Season s : seasons) {
            if (s == season) {
                inSeason = true;
            }
        }
        return inSeason;
    }

    public double getProductPrice(Season season) {
        if (isInSeason(season)) {
            return getPrice();
        }
        return getOutOfSeasonPrice();
    }

    public ItemType getIngredient() {
        return ingredient;
    }

    public Integer getFishingSkill() {
        return fishingSkill;
    }
}
