package com.example.models;

import com.example.Repositories.GameRepository;
import com.example.models.enums.Quality;
import com.example.models.enums.recipes.CookingRecipes;
import com.example.models.enums.recipes.CraftingRecipes;
import com.example.models.enums.types.AnimalType;
import com.example.models.enums.types.itemTypes.MiscType;
import com.example.models.enums.worldEnums.Season;
import com.example.models.enums.worldEnums.Weather;
import com.example.models.items.Item;
import com.example.models.items.Misc;
import com.example.models.mapModels.Farm;
import com.example.models.mapModels.Map;
import com.example.models.mapObjects.AnimalBlock;
import com.example.models.skills.Skill;
import com.example.views.gameViews.GameThread;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Transient;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Entity("games")
public class Game {
    @Id
    private ObjectId _id;
    private ArrayList<Player> players;
    private Map map;
    private boolean isGameOngoing;
    private Player currentPlayer;
    private LocalDateTime date;
    private Weather weatherToday;
    private Weather weatherTomorrow;
    private Season season;
    @Transient
    private GameThread gameThread;
    public boolean hasTurnCycleFinished;

    public void advanceTime() {
        date = date.plusHours(1);
        hasTurnCycleFinished = false;
        if (date.getHour() == 23) {
            //TODO : next day has arrived
            date = date.plusHours(10);

            gameThread.handleRefreshForaging();

            weatherToday = weatherTomorrow;

            determineAndSetWeatherTomorrow();

            resetAllAnimalDailyVariables();

            if (weatherToday == Weather.STORM) {
                strikeLightningOnStormyDay();
            }
            for (Player player : players) {
                if (player.isPlayerFainted()) {
                    player.setPlayerFainted(false);
                    player.setEnergy(player.getMaxEnergy() * 0.75);
                } else {
                    player.setEnergy(player.getMaxEnergy());
                }
                player.setUsedEnergyInTurn(0);
            }
        }
        if (date.getDayOfMonth() == 29) {
            date = date.minusDays(28);
            date = date.plusMonths(1);
        }
    }

    private void strikeLightningOnStormyDay() {
        for (int i = 0; i < 3; i++) {
            int targetX = (int) (Math.random() * 75);
            int targetY = (int) (Math.random() * 50);
            currentPlayer.getFarm().strikeLightning(targetX, targetY);
        }
    }

    private void determineAndSetWeatherTomorrow() {
        int randomNumber;
        do {
            randomNumber = (int) (Math.random() * 4);
        } while (!Weather.values()[randomNumber]
                .isWeatherPossibleInThisSeason(App.getLoggedInUser().getCurrentGame().getSeason()));
        weatherTomorrow = Weather.values()[randomNumber];
    }

    public boolean checkSeasonChange() {
        if (date.getMonthValue() >= 1 && date.getMonthValue() <= 3) {
            season = Season.SPRING;
            return true;
        } else if (date.getMonthValue() >= 4 && date.getMonthValue() <= 6) {
            season = Season.SUMMER;
            return true;
        } else if (date.getMonthValue() >= 7 && date.getMonthValue() <= 9) {
            season = Season.FALL;
            return true;
        } else if (date.getMonthValue() >= 10 && date.getMonthValue() <= 12) {
            season = Season.WINTER;
            return true;
        }
        return false;
    }

    public Game() {

    }

    public void resetAllAnimalDailyVariables() {
        //TODO: Handle later
        User user = App.getLoggedInUser();
        Game game = user.getCurrentGame();
        Farm farm = game.getCurrentPlayer().getFarm();
        ArrayList<Animal> animals = game.getCurrentPlayer().getAnimals();
        for (Animal animal : animals) {
            if (animal.hasBeenPetToDay) {
                animal.setXp(animal.getXp() + 15);
            } else
                animal.setXp(animal.getXp() - 10);
            animal.hasBeenPetToDay = false;
            if (animal.hasBeenFedByHay || animal.hasBeenFedByGrass)
                animal.hasBeenFedYesterday = true;
            if (!animal.hasBeenFedByGrass && !animal.hasBeenFedByHay)
                animal.setXp(animal.getXp() - 20);
            AnimalBlock animalBlock = farm.getAnimalBlock(animal);
            if (animalBlock != null) {
                animal.setXp(animal.getXp() - 20);
            }
            if (animal.getType().equals(AnimalType.SHEEP)) {
                if (animal.hasBeenHarvested)
                    animal.setXp(animal.getXp() + 5);
            }
            if (animal.hasBeenFedYesterday) {
                double randNum = 0.5 + Math.random();
                randNum = (animal.getXp() + (150 * randNum)) / 1500;
                double qualityNumber = ((double) animal.getXp() / 1000) * (0.5 + 0.5 * Math.random());
                Quality quality = Quality.IRIDIUM;
                if(qualityNumber <0.5)
                    quality = Quality.COPPER;
                else if(qualityNumber <0.7)
                    quality = Quality.SILVER;
                else if(qualityNumber <0.9)
                    quality = Quality.GOLD;
                Item item = null;
                if (randNum >= 1 && animal.getXp() >= 100) {
                    if (animal.getType().equals(AnimalType.Chicken))
                        item = new Misc(MiscType.BIG_EGG, quality);
                    else if (animal.getType().equals(AnimalType.DUCK))
                        item = new Misc(MiscType.DUCK_FEATHER, quality);
                    else if (animal.getType().equals(AnimalType.RABBIT))
                        item = new Misc(MiscType.RABBITS_FOOT, quality);
                    else if (animal.getType().equals(AnimalType.DINOSAUR))
                        item = new Misc(MiscType.DINOSAUR, quality);
                    else if (animal.getType().equals(AnimalType.COW))
                        item = new Misc(MiscType.BIG_MILK, quality);
                    else if (animal.getType().equals(AnimalType.GOAT))
                        item = new Misc(MiscType.BIG_GOAT_MILK, quality);
                    else if (animal.getType().equals(AnimalType.SHEEP))
                        item = new Misc(MiscType.WOOL, quality);
                    else if (animal.getType().equals(AnimalType.PIG) && animal.hasBeenFedByGrass)
                        item = new Misc(MiscType.TRUFFLE, quality);
                } else {
                    if (animal.getType().equals(AnimalType.Chicken))
                        item = new Misc(MiscType.EGG, quality);
                    else if (animal.getType().equals(AnimalType.DUCK))
                        item = new Misc(MiscType.DUCK_EGG, quality);
                    else if (animal.getType().equals(AnimalType.RABBIT))
                        item = new Misc(MiscType.WOOL, quality);
                    else if (animal.getType().equals(AnimalType.DINOSAUR))
                        item = new Misc(MiscType.DINOSAUR, quality);
                    else if (animal.getType().equals(AnimalType.COW))
                        item = new Misc(MiscType.MILK, quality);
                    else if (animal.getType().equals(AnimalType.GOAT))
                        item = new Misc(MiscType.GOAT_MILK, quality);
                    else if (animal.getType().equals(AnimalType.SHEEP))
                        item = new Misc(MiscType.WOOL, quality);
                    else if (animal.getType().equals(AnimalType.PIG) && animal.hasBeenFedByGrass)
                        item = new Misc(MiscType.TRUFFLE, quality);
                }
                animal.hasBeenFedByGrass = false;
                animal.hasBeenFedByHay = false;
                animal.product = item;
            }
        }
        GameRepository.saveGame(game);
    }

    public Game(ArrayList<Player> players, Player currentPlayer) {
        this.hasTurnCycleFinished = false;
        this.players = players;
        this.map = Map.makeMap();
        this.isGameOngoing = true;
        this.currentPlayer = currentPlayer;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = LocalDateTime.parse("2025-01-01 09:00:00", dateTimeFormatter);
        this.weatherToday = Weather.SUNNY;
        this.weatherTomorrow = Weather.SUNNY;
        this.season = Season.SPRING;
        this.isGameOngoing = false;
    }

    public void checkForRecipeUnlocking() {
        for (Player player : players) {
            for (CraftingRecipes craftingRecipes : CraftingRecipes.values()) {
                if (!player.getUnlockedCraftingRecipes().contains(craftingRecipes)
                        && isPlayerLevelOk(player, craftingRecipes.farmingLevel
                        , craftingRecipes.foragingLevel, craftingRecipes.fishingLevel
                        , craftingRecipes.miningLevel)) {
                    player.getUnlockedCraftingRecipes().add(craftingRecipes);
                }
            }
            for (CookingRecipes cookingRecipes : CookingRecipes.values()) {
                if (!player.getUnlockedCookingRecipes().contains(cookingRecipes)
                        && isPlayerLevelOk(player, cookingRecipes.farmingLevel
                        , cookingRecipes.foragingLevel, cookingRecipes.fishingLevel
                        , cookingRecipes.miningLevel)) {
                    player.getUnlockedCookingRecipes().add(cookingRecipes);
                }
            }
        }
    }

    private boolean isPlayerLevelOk(Player player, int farmingLevel
            , int foragingLevel, int fishingLevel, int miningLevel) {
        return player.getUnbuffedFarmingSkill().getLevel().levelNumber >= farmingLevel &&
                player.getUnbuffedForagingSkill().getLevel().levelNumber >= foragingLevel &&
                player.getUnbuffedFishingSkill().getLevel().levelNumber >= fishingLevel &&
                player.getUnbuffedMiningSkill().getLevel().levelNumber >= miningLevel;
    }

    public void checkForSkillUpgrades() {
        for (Player player : players) {
            ArrayList<Skill> skills = new ArrayList<>();
            skills.add(player.getUnbuffedFarmingSkill());
            skills.add(player.getUnbuffedFishingSkill());
            skills.add(player.getUnbuffedForagingSkill());
            skills.add(player.getUnbuffedMiningSkill());
            for (Skill skill : skills) {
                if (skill.getLevel().getXpToNextLevel() == Double.POSITIVE_INFINITY) {
                    continue;
                }
                if (skill.getLevel().xpToNextLevel <= skill.getXp()) {
                    System.out.println(player.getUser().getUsername() + "'s " + skill + " Skill has leveled up to level "
                            + skill.getLevel().getNextLevel().toString());
                    skill.setXp((int) (skill.getXp() - skill.getLevel().getXpToNextLevel()));
                    skill.setLevel(skill.getLevel().getNextLevel());
                }
            }
        }
    }

    public void handleBuffExpiration() {
        for (Player player : players) {
            player.getActiveBuffs().removeIf(buff -> buff.getExpirationTime().isBefore(date));
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Map getMap() {
        return map;
    }

    public boolean isGameOngoing() {
        return isGameOngoing;
    }

    public void setGameOngoing(boolean gameOngoing) {
        isGameOngoing = gameOngoing;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Weather getWeatherToday() {
        return weatherToday;
    }

    public void setWeatherToday(Weather weatherToday) {
        this.weatherToday = weatherToday;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public GameThread getGameThread() {
        return gameThread;
    }

    public void setGameThread(GameThread gameThread) {
        this.gameThread = gameThread;
    }

    public Weather getWeatherTomorrow() {
        return weatherTomorrow;
    }

    public void setWeatherTomorrow(Weather weatherTomorrow) {
        this.weatherTomorrow = weatherTomorrow;
    }

    public ObjectId get_id() {
        return _id;
    }

    public Player findPlayerByUser(User user) {
        for (Player player : players) {
            if (player.getUser().getUsername().equals(user.getUsername())) {
                return player;
            }
        }
        return null;
    }

    /// Cycles to next player in turn, returns true if end was reached.
    public boolean cycleToNextPlayer() {
        int index = players.indexOf(currentPlayer);
        if (index == players.size() - 1) {
            currentPlayer = players.getFirst();
            return true;
        } else {
            currentPlayer = players.get(index + 1);
            return false;
        }
    }
}
