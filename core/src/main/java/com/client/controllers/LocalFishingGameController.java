package com.client.controllers;

import com.badlogic.gdx.math.MathUtils;
import com.client.views.inGameMenus.FishingMiniGame;
import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.enums.Quality;
import com.common.models.enums.types.itemTypes.FishType;
import com.common.models.enums.worldEnums.Season;
import com.common.models.enums.worldEnums.Weather;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalFishingGameController {
    private static final float SPEED_FACTOR = 15;
    private static final float WATER_LANE_HEIGHT = 427;
    private static final float BEHAVIOUR_RESET_TIME = 0.5f;
    private static final float BASE_ACCELERATION = 2;
    private static float behaviourTimer = 0.0f;
    private static FishMotionTypes fishMotionType;
    private static FishMoves recentFishMoves;

    public static void determineFishMotionType(boolean isLegendary) {
        if (isLegendary) {
            fishMotionType = FishMotionTypes.DART;
        } else {
            if (randomQuery(60)) {
                fishMotionType = FishMotionTypes.SMOOTH;
            } else if (randomQuery(50)) {
                fishMotionType = FishMotionTypes.MIXED;
            } else if (randomQuery(50)) {
                fishMotionType = FishMotionTypes.SINKER;
            } else {
                fishMotionType = FishMotionTypes.FLOATER;
            }
        }
        recentFishMoves = FishMoves.UP;
    }

    public static void handleFishAI(FishingMiniGame fishingMiniGame, float delta) {
        fishingMiniGame.incrementFishPosition(delta * SPEED_FACTOR * fishingMiniGame.getFishVelocity());
        fishingMiniGame.setFishPosition(
            MathUtils.clamp(fishingMiniGame.getFishPosition(), 0, WATER_LANE_HEIGHT - fishingMiniGame.getFishImage().getHeight()));

        if (fishingMiniGame.getFishPosition() == WATER_LANE_HEIGHT - fishingMiniGame.getFishImage().getHeight()) {
            fishingMiniGame.incrementFishPosition(-0.5f);
            fishingMiniGame.setFishVelocity(-0.6f * fishingMiniGame.getFishVelocity());
            fishingMiniGame.setFishAcceleration(0);
        } else if (fishingMiniGame.getFishPosition() == 0) {
            fishingMiniGame.incrementFishPosition(0.5f);
            fishingMiniGame.setFishVelocity(-0.6f * fishingMiniGame.getFishVelocity());
            fishingMiniGame.setFishAcceleration(0);
        }

        fishingMiniGame.getFishHitbox().y = fishingMiniGame.BOBBER_BASE_Y + fishingMiniGame.getFishPosition();
        fishingMiniGame.getFishImage().setY(fishingMiniGame.BOBBER_BASE_Y + fishingMiniGame.getFishPosition());

        fishingMiniGame.incrementFishVelocity(fishingMiniGame.getFishAcceleration() * delta * SPEED_FACTOR);

        behaviourTimer += delta;

        if (behaviourTimer >= BEHAVIOUR_RESET_TIME) {
            behaviourTimer = 0;

            int rand = (int) (Math.random() * 3);
            FishMoves move = FishMoves.values()[rand];

            if (fishMotionType == FishMotionTypes.SMOOTH && randomQuery(70)) {
                move = recentFishMoves;
            }

            recentFishMoves = move;

            if (move == FishMoves.UP) {
                fishingMiniGame.setFishVelocity(fishMotionType.speed);

                if (fishMotionType == FishMotionTypes.FLOATER) {
                    fishingMiniGame.setFishAcceleration(BASE_ACCELERATION);
                }
            } else if (move == FishMoves.DOWN) {
                fishingMiniGame.setFishVelocity(-fishMotionType.speed);

                if (fishMotionType == FishMotionTypes.SINKER) {
                    fishingMiniGame.setFishAcceleration(-BASE_ACCELERATION);
                }
            } else {
                fishingMiniGame.setFishVelocity(0);
            }
        }
    }

    public static HashMap<String, Object> queryFishingResult(GameData gameData, Quality poleQuality) {

        int randomNumber = (int) (Math.random() * 2);
        double weatherModifier = setWeatherModifierFishing(gameData);
        Player player = gameData.getCurrentPlayer();
        int playerLevel = player.getFishingSkill().getLevel().levelNumber;
        int numberOfFishes = (int) (((double) randomNumber)
            * weatherModifier * (double) (playerLevel + 2)) + 1;

        ArrayList<FishType> values = getValidFishTypes(gameData.getSeason(), playerLevel);
        int randomFishNumber = (int) (Math.random() * values.size());
        FishType fishType = values.get(randomFishNumber);

        double qualityNumber;
        double pole = setPoleModifier(poleQuality);
        qualityNumber = (randomNumber * (double) (playerLevel + 2) * pole) / (7.0 - weatherModifier);
        Quality fishQuality = setFishQuality(qualityNumber);
        int gainedXp = 5;

        var result = new HashMap<String, Object>();
        result.put("type", fishType);
        result.put("quality", fishQuality);
        result.put("quantity", numberOfFishes);
        result.put("xp", gainedXp);
        return result;
    }

    private static ArrayList<FishType> getValidFishTypes(Season season, int playerLevel) {
        if (playerLevel == 4) {
            FishType[] values = FishType.values();
            ArrayList<FishType> finalValues = new ArrayList<>();
            for (int i = 0; i < values.length; i++) {
                if (values[i].season == season) {
                    finalValues.add(values[i]);
                }
            }
            return finalValues;
        }
        FishType[] values = FishType.values();
        ArrayList<FishType> finalValues = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            if (values[i].season == season && !values[i].isLegendary) {
                finalValues.add(values[i]);
            }
        }
        return finalValues;
    }

    private static Quality setFishQuality(double qualityNumber) {
        if (qualityNumber >= 0.5 && qualityNumber < 0.7)
            return Quality.SILVER;
        else if (qualityNumber >= 0.7 && qualityNumber < 0.9)
            return Quality.GOLD;
        else if (qualityNumber >= 0.9)
            return Quality.IRIDIUM;
        return Quality.COPPER;
    }

    private static double setPoleModifier(Quality quality) {
        if (quality == Quality.COPPER)
            return 0.1;
        else if (quality == Quality.SILVER)
            return 0.5;
        else if (quality == Quality.GOLD)
            return 0.9;
        return 1.2;
    }

    private static double setWeatherModifierFishing(GameData gameData) {
        double weatherModifier;
        if (gameData.getWeatherToday().equals(Weather.SUNNY))
            weatherModifier = 1.5;
        else if (gameData.getWeatherToday().equals(Weather.RAIN))
            weatherModifier = 1.2;
        else if (gameData.getWeatherToday().equals(Weather.STORM))
            weatherModifier = 0.5;
        else
            weatherModifier = 1.0;
        return weatherModifier;
    }

    private static boolean randomQuery(int successPercent) {
        return (int) (Math.random() * 100) < successPercent;
    }

    private enum FishMotionTypes {
        MIXED(5),
        SMOOTH(5),
        SINKER(5),
        FLOATER(5),
        DART(9);

        public final int speed;

        FishMotionTypes(int speed) {
            this.speed = speed;
        }
    }

    private enum FishMoves {
        UP,
        DOWN,
        STATIONARY
    }
}
