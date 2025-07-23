package com.server.routers;

import com.server.Middlewares.Auth;
import com.server.controllers.GameController;
import io.javalin.Javalin;

public class GameRouter {
    private final Javalin app;
    private final GameController controller = new GameController();

    public GameRouter(Javalin app) {
        this.app = app;
    }

    public void initializeRoutes() {
        app.before("/api/game/*", Auth.validate);

        // GET requests
        app.get("/api/game/startGame/{lobbyId}", controller::startGame);
        app.get("/api/game/loadGame/{gameId}", controller::loadGame);
        app.get("/api/game/{gameId}", controller::handleGetRequests);

        // POST requests
        app.post("/api/game/{gameId}/{controllerName}", controller::handlePostRequests);
    }
}
