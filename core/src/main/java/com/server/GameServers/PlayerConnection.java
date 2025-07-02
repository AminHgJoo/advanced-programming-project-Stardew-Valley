package com.server.GameServers;

import io.javalin.websocket.WsContext;

public class PlayerConnection {
    private String playerId;
    private WsContext wsContext;

    public PlayerConnection(String playerId, WsContext wsContext) {
        this.playerId = playerId;
        this.wsContext = wsContext;
    }

    public void send(String message) {
        wsContext.send(message);
    }
}
