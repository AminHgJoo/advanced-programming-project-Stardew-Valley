package com.example.models;

import com.example.models.enums.Season;
import com.example.models.enums.Weather;
import com.example.models.mapModels.Map;
import com.example.views.GameThread;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Entity("games")
public class Game {
    @Id
    private ObjectId _id;
    final private ArrayList<Player> players;
    final private Map map;
    private boolean isGameOngoing;
    private Player currentPlayer;
    private LocalDateTime date;
    private Weather weatherToday;
    private Weather weatherTomorrow;
    private Season season;
    private final GameThread timeHandler;
    private boolean hasTurnCycleFinished;
    //TODO : handle turn cycle boolean!

    public void advanceTime() {
        if (hasTurnCycleFinished) {
            date = date.plusHours(1);
            hasTurnCycleFinished = false;
            if (date.getHour() == 23) {
                //TODO : next day has arrived
                date = date.plusHours(10);
                weatherToday = weatherTomorrow;
                determineWeatherTomorrow();
            }
            if (date.getDayOfMonth() == 29) {
                date = date.minusDays(28);
                date = date.plusMonths(1);
            }
        }
    }

    private void determineWeatherTomorrow() {
        int randomNumber;
        do {
            randomNumber = (int) (Math.random() * 4);
        } while (!Weather.values()[randomNumber]
                .isWeatherPossibleInThisSeason(App.getLoggedInUser().getCurrentGame().getSeason()));
        weatherTomorrow = Weather.values()[randomNumber];
    }

    public void checkSeasonChange() {
        if (date.getMonthValue() >= 1 && date.getMonthValue() <= 3) {
            season = Season.SPRING;
        } else if (date.getMonthValue() >= 4 && date.getMonthValue() <= 6) {
            season = Season.SUMMER;
        } else if (date.getMonthValue() >= 7 && date.getMonthValue() <= 9) {
            season = Season.AUTUMN;
        } else if (date.getMonthValue() >= 10 && date.getMonthValue() <= 12) {
            season = Season.WINTER;
        }
    }

    public Game(ArrayList<Player> players, Player currentPlayer) {
        this.players = players;
        this.map = Map.makeMap();
        this.isGameOngoing = true;
        this.currentPlayer = currentPlayer;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = LocalDateTime.parse("2025-01-01 09:00:00", dateTimeFormatter);
        this.weatherToday = Weather.SUNNY;
        this.weatherTomorrow = Weather.SUNNY;
        this.season = Season.SPRING;
        this.timeHandler = new GameThread(this);
        this.isGameOngoing = true;
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

    public GameThread getTimeHandler() {
        return timeHandler;
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
}
