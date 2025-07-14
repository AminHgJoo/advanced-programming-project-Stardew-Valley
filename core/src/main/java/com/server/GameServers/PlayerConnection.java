package com.server.GameServers;

import io.javalin.websocket.WsContext;

public class PlayerConnection {
    private String username;
    private WsContext wsContext;

    public PlayerConnection(String username, WsContext wsContext) {
        this.username = username;
        this.wsContext = wsContext;
    }

    public void send(String message) {
        try {
            wsContext.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }
}
