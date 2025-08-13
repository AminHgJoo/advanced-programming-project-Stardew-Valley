package com.client.utils;

import com.client.ClientApp;
import com.common.models.User;
import com.google.gson.Gson;
import com.server.GameServers.AppWebSocket;
import com.server.repositories.LobbyRepository;
import com.server.repositories.UserRepository;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleWebSocketClient extends WebSocketClient {
    public SimpleWebSocketClient(String serverUri) {
        super(URI.create(serverUri + "?playerUsername=" + ClientApp.loggedInUser.getUsername()));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected!");
    }

    @Override
    public void onMessage(String message) {
        if (ClientApp.currentScreen != null)
            ClientApp.currentScreen.socketMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected: " + code);
        (new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                SimpleWebSocketClient wsClient = new SimpleWebSocketClient("ws://" + System.getenv("host") + "/game");
                wsClient.connect();
                if (wsClient.isOpen()) {
                    ClientApp.client = wsClient;
                    this.cancel();
                }
            }
        }, 2 * 60 * 1000, 2 * 60 * 1000);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

}
