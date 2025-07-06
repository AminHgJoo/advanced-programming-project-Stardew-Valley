package com.server.routers;

import com.server.Middlewares.Auth;
import com.server.controllers.LobbyController;
import io.javalin.Javalin;

public class LobbyRouter {
    private final Javalin app;
    private final LobbyController controller = new LobbyController();
    public LobbyRouter(Javalin app) {
        this.app = app;
    }

    public void initializeRoutes() {
        app.before("/api/lobby/*" , Auth.validate);

        // GET requests
        app.get("/api/lobby/{id}", controller::getLobbyById);
        app.get("/api/lobby/all" , controller::getAllLobbies);
        app.get("/api/lobby/getLobbiesByOwnerUsername/{ownerUsername}" , controller::getLobbiesByOwnerUsername);
        app.get("/api/lobby/getLobbyByName/{name}" , controller::getLobbyByName);

        // POST requests
        app.post("/api/lobby/" , controller::createLobby);
    }
}
