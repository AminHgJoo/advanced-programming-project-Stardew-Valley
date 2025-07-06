package com.server.routers;

import com.server.Middlewares.Auth;
import com.server.controllers.UserController;
import io.javalin.Javalin;

public class UserRouter {
    private final Javalin app;
    private final UserController controller = new UserController();

    public UserRouter(Javalin app) {
        this.app = app;
    }

    public void initializeRoutes() {
        app.before("/api/user/whoAmI",Auth.validate).get("/api/user/whoAmI" , controller::whoAmI);

        app.post("/api/user/register", controller::register);

        app.post("/api/user/login", controller::login);

        app.post("/api/user/forgetPassword", controller::forgetPassword);

        app.before("/api/user/changePassword",Auth.validate).post("/api/user/changePassword", controller::changePassword);

        app.before("/api/user/changeEmail",Auth.validate).post("/api/user/changeEmail", controller::changeEmail);

        app.before("/api/user/changeUsername",Auth.validate).post("/api/user/changeUsername", controller::changeUsername);

        app.before("/api/user/changeNickname",Auth.validate).post("/api/user/changeNickname", controller::changeNickname);
    }
}
