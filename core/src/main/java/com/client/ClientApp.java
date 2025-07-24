package com.client;

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
    public static String token = "";
    public static SimpleWebSocketClient client = null;

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
