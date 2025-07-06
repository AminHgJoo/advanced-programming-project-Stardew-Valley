package com.client.utils;

import com.common.models.User;
import com.common.models.enums.SecurityQuestion;
import com.common.models.networking.Lobby;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ModelDecoder {
    public static User decodeUser(String json) {
        try {
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
                if (obj.getString("currentGameId") != null) {
                    ObjectId gameId = new ObjectId(obj.getString("currentGameId"));
                    user.setCurrentGameId(gameId.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (obj.getString("currentLobbyId") != null) {
                    ObjectId gameId = new ObjectId(obj.getString("currentLobbyId"));
                    user.setCurrentLobbyId(gameId.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Lobby decodeLobby(String json) {
        try {
            Lobby lobby = new Lobby();
            JSONObject obj = new JSONObject(json);
            lobby.setName(obj.getString("name"));
            try {
                lobby.setVisible(obj.getBoolean("visible"));
                lobby.setPublic(obj.getBoolean("public"));
            }catch (Exception e){
                lobby.setVisible(obj.getBoolean("isVisible"));
                lobby.setPublic(obj.getBoolean("isPublic"));
            }
            lobby.setOwnerUsername(obj.getString("ownerUsername"));
            ObjectId id = new ObjectId(obj.getString("_id"));
            lobby.set_id(id.toString());
            JSONArray users = obj.getJSONArray("users");
            for (int i = 0; i < users.length(); i++) {
                String userString = users.getString(i);
                lobby.getUsers().add(userString);
            }
            JSONObject farm = obj.getJSONObject("usersFarm");
            if (farm != null) {
                HashMap<String, Integer> farmFarm = new HashMap<>();
                for (Object key : farm.keySet()) {
                    farmFarm.put(key.toString(), farm.getInt(key.toString()));
                }
                lobby.setUsersFarm(farmFarm);
            } else {
                lobby.setUsersFarm(new HashMap<>());

            }
            return lobby;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
