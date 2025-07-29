package com.server.routers;

import com.server.Middlewares.Auth;
import com.server.controllers.GameController;
import com.server.controllers.InGameControllers.MusicController;
import io.javalin.Javalin;
import io.javalin.http.UploadedFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class GameRouter {
    private final Javalin app;
    private final GameController controller = new GameController();
    private final MusicController musicController = new MusicController();

    public GameRouter(Javalin app) {
        this.app = app;
    }

    public void initializeRoutes() {
        app.before("/api/game/*", Auth.validate);

        // GET requests
        app.get("/api/game/startGame/{lobbyId}", controller::startGame);
        app.get("/api/game/loadGame/{gameId}", controller::loadGame);
        app.get("/api/game/leave/{gameId}", controller::leaveGame);
        app.get("/api/game/{gameId}/music/download/{filename}", musicController::downloadMusicHandler);

        // POST requests
        app.post("/api/game/{gameId}/{controllerName}", controller::handlePostRequests);
        app.post("/api/game/{gameId}/music/upload", musicController::uploadMusicHandler);
        app.post("/api/game/{gameId}/music/sync_req", musicController::handleMusicSyncing);
        app.post("/api/game/{gameId}/music/sync_res", musicController::receiveMusicData);
    }
}
