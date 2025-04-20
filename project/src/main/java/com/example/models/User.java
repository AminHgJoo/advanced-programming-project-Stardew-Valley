package com.example.models;

import com.example.Repositories.GameRepository;
import com.example.models.enums.SecurityQuestion;
import com.example.views.GameThread;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.ArrayList;

@Entity("users")
public class User {
    @Id
    private ObjectId _id;
    private String username;
    private String hashedPassword;
    private String nickname;
    private String email;
    private String gender;
    private SecurityQuestion question;
    private String answer;
    private int moneyHighScore;
    private int numberOfGames;
    @Reference(lazy = true , idOnly = true , ignoreMissing = true)
    private Game currentGame;
    @Reference(lazy = true , idOnly = true , ignoreMissing = true)
    private final ArrayList<Game> games = new ArrayList<>();

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
        this.currentGame = null;
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
        this.currentGame = null;
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

    public Game getCurrentGame() {
        if(currentGame == null) return null;
        if(currentGame.get_id() == null) return currentGame;
        GameThread t = currentGame.getGameThread();
        populateGame();
        currentGame.setGameThread(t);
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void populateGame() {
        currentGame = GameRepository.findGameById(currentGame.get_id().toString(), true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }
}
