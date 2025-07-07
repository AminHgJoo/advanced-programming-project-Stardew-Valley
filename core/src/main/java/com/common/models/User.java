package com.common.models;

import com.common.models.enums.SecurityQuestion;
import com.server.repositories.GameRepository;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Transient;

import java.util.ArrayList;

@Entity("users")
public class User {
    private final ArrayList<String> games = new ArrayList<String>();
    @Id
    private String _id;
    private String username;
    private String hashedPassword;
    private String nickname;
    private String email;
    private String gender;
    private SecurityQuestion question;
    private String answer;
    private int moneyHighScore;
    private int numberOfGames;
    private String currentGameId;
    private String currentLobbyId;
    @Transient
    private GameData currentGameData;

    public User() {

    }

    /// Does not have any usages. set to private to prevent errors and mistakes.
    private User(SecurityQuestion question, String answer, String gender
        , String email, String nickname, String password, String username) {
        this.question = question;
        this.answer = answer;
        this.gender = gender;
        this.email = email;
        this.nickname = nickname;
        this.hashedPassword = password;
        this.username = username;
        this.moneyHighScore = 0;
        this.numberOfGames = 0;
        this.currentGameData = null;
    }

    public User(String gender
        , String email, String nickname, String password, String username) {
        this.gender = gender;
        this.email = email;
        this.nickname = nickname;
        this.hashedPassword = password;
        this.username = username;
        this.moneyHighScore = 0;
        this.numberOfGames = 0;
        this.currentGameData = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public SecurityQuestion getQuestion() {
        return question;
    }

    public void setQuestion(SecurityQuestion question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getMoneyHighScore() {
        return moneyHighScore;
    }

    public void setMoneyHighScore(int moneyHighScore) {
        this.moneyHighScore = moneyHighScore;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public GameData getCurrentGame() {
        if (currentGameData == null && currentGameId != null) {
            currentGameData = GameRepository.findGameById(currentGameId, true);
        }
        return currentGameData;
    }

    public void setCurrentGame(GameData currentGameData) {
        this.currentGameData = currentGameData;
    }

    public ArrayList<String> getGames() {
        return games;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void populateGame() {
        currentGameData = GameRepository.findGameById(currentGameData.get_id().toString(), true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    public String getCurrentGameId() {
        return currentGameId;
    }

    public void setCurrentGameId(String currentGameId) {
        this.currentGameId = currentGameId;
    }

    public String getCurrentLobbyId() {
        return currentLobbyId;
    }

    public void setCurrentLobbyId(String currentLobbyId) {
        this.currentLobbyId = currentLobbyId;
    }
}
