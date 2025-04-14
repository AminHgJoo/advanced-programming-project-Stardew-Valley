package com.example.models;

import com.example.models.enums.Season;
import com.example.models.enums.Weather;
import com.example.models.mapModels.Map;
import com.example.views.GameThread;

import com.example.java.time.LocalDateTime;
import com.example.java.time.format.DateTimeFormatter;
import com.example.java.util.ArrayList;
import com.example.java.util.Date;

public class Game {
    final private ArrayList<Player> players;
    final private Map map;
    private boolean isGameOngoing;
    private Player currentPlayer;
    private LocalDateTime date;
    private Weather weather;
    private Season season;
    private final GameThread timeHandler;

    public void advanceTime() {

    }

    public Game(ArrayList<Player> players, Map map, Player currentPlayer) {
        this.players = players;
        this.map = map;
        this.isGameOngoing = true;
        this.currentPlayer = currentPlayer;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = LocalDateTime.parse("2025-03-20 09:00:00", dateTimeFormatter);
        this.weather = Weather.SUNNY;
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

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
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
}
