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
        app.before("/api/game/*" , Auth.validate);

        // GET requests
        app.get("/api/game/startGame/{lobbyId}" , controller::startGame);

        // POST requests
    }
}
