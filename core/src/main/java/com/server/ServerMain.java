package com.server;

import com.server.routers.GameRouter;
import com.server.routers.UserRouter;
import com.server.utilities.Connection;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;

public class ServerMain {
    public static void main(String[] args) {
        Dotenv.configure()
            .directory(System.getProperty("user.dir") + "/core/src/main/java/com/server/ENV")
            .filename("env.prod")
            .systemProperties()
            .load();
        Connection.getDatabase();

        Javalin app = Javalin.create().start(8080);
        app.get("/", ctx -> {
            ctx.result("Hello World!");
        });

        System.out.println("Server is listening http://localhost:8080 ...");

        // User routes
        new UserRouter(app).initializeRoutes();

        // Game routes
        new GameRouter(app).initializeRoutes();
    }
}
