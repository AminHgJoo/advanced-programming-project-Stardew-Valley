package com.client.utils;

import com.common.models.User;
import com.common.models.enums.SecurityQuestion;
import org.bson.types.ObjectId;
import org.json.JSONObject;

public class UserDecoder {
    public static User decode(String json) {
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
}
