package com.server;

import com.server.routers.GameRouter;
import com.server.routers.UserRouter;
import io.javalin.Javalin;

public class ServerMain {
    public static void main(String[] args) {
        //Usage : java ServerMain <port>
        Javalin app = Javalin.create().start(8080);
        System.out.println("Server is listening http://localhost:8080 ...");

        // User routes
        new UserRouter(app).initializeRoutes();

        // Game routes
        new GameRouter(app).initializeRoutes();
    }
}
