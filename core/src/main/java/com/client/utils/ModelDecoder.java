package com.client.utils;

import com.common.models.User;
import com.common.models.enums.SecurityQuestion;
import com.common.models.networking.Lobby;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

public class ModelDecoder {
    public static User decodeUser(String json) {
        User user = new User();
        JSONObject obj = new JSONObject(json);
        user.setUsername(obj.getString("username"));
        user.setHashedPassword(obj.getString("hashedPassword"));
        user.setNickname(obj.getString("nickname"));
        user.setQuestion(SecurityQuestion.valueOf(obj.getString("question")));
        user.setAnswer(obj.getString("answer"));
        user.setEmail(obj.getString("email"));
        user.setGender(obj.getString("gender"));
        user.setMoneyHighScore(obj.getInt("moneyHighScore"));
        user.setNumberOfGames(obj.getInt("numberOfGames"));
        ObjectId id = new ObjectId(obj.getString("_id"));
        user.set_id(id.toString());
        try {

            if (obj.getJSONObject("currentGameId") != null) {
                ObjectId gameId = new ObjectId(obj.getString("currentGameId"));
                user.setCurrentGameId(gameId.toString());
            }
        } catch (Exception e) {
        }
        return user;
    }

    public static Lobby decodeLobby(String json) {
        Lobby lobby = new Lobby();
        JSONObject obj = new JSONObject(json);
        lobby.setName(obj.getString("name"));
        lobby.setVisible(obj.getBoolean("visible"));
        lobby.setPublic(obj.getBoolean("public"));
        lobby.setOwnerUsername(obj.getString("ownerUsername"));
        ObjectId id = new ObjectId(obj.getString("_id"));
        lobby.set_id(id.toString());
        JSONArray users = obj.getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            String userString = users.getString(i);
            lobby.getUsers().add(userString);
        }
        return lobby;
    }
}
