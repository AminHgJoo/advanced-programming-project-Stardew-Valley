package com.client;

import com.badlogic.gdx.graphics.Texture;
import com.client.utils.MyScreen;
import com.client.utils.SimpleWebSocketClient;
import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.User;

public class ClientApp {

    public static User loggedInUser;
    public static Player currentPlayer;
    public static GameData currentGameData;
    public static MyScreen currentScreen;
    public static SimpleWebSocketClient client = null;
    public static String avatarPath = "avatars/avatar1.jpg";
    public static Texture avatarTexture = new Texture(avatarPath);
    public static String token = "";

    public static void init() {
        try {
            SimpleWebSocketClient wsClient = new SimpleWebSocketClient("ws://localhost:8080/game");
            wsClient.connect();
            client = wsClient;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
