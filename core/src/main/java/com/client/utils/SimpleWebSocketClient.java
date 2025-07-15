package com.client.utils;

import com.client.ClientApp;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

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
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
