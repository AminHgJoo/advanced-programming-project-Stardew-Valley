package com.client;

import com.common.models.User;
import com.server.utilities.SimpleWebSocketClient;

public class ClientApp {
    public static User loggedInUser;
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
