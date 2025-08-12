package com.server;

import com.server.GameServers.AppWebSocket;
import com.server.routers.GameRouter;
import com.server.routers.LobbyRouter;
import com.server.routers.UserRouter;
import com.server.utilities.Connection;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.config.MultipartConfig;
import io.javalin.config.SizeUnit;

public class ServerMain {
    public static void main(String[] args) {
        Dotenv.configure()
            .directory(System.getProperty("user.dir") + (System.getProperty("user.dir").contains("core") ? "" : "/core") + "/src/main/java/com/server/ENV")
            .filename("env.prod")
            .systemProperties()
            .load();
        Connection.getDatabase();

        Javalin app = Javalin.create((JavalinConfig config) -> {
            var multipartConfig = new MultipartConfig();
            multipartConfig.cacheDirectory("/tmp");
            multipartConfig.maxFileSize(20, SizeUnit.MB);
            multipartConfig.maxInMemoryFileSize(20, SizeUnit.MB);
            multipartConfig.maxTotalRequestSize(20, SizeUnit.MB);
            config.jetty.multipartConfig = multipartConfig;
        }).start(8080);

        app.get("/", ctx -> {
            ctx.result("Hello World!");
        });

        new AppWebSocket(app).start();

        System.out.println("Server is listening http://localhost:8080 ...");

        // User routes
        new UserRouter(app).initializeRoutes();

        // Game routes
        new GameRouter(app).initializeRoutes();

        // Lobby routes
        new LobbyRouter(app).initializeRoutes();

        app.exception(Exception.class, (e, ctx) -> {
        });
    }
}
