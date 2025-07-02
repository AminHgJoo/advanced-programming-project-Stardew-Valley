package com.server.Middlewares;

import com.server.utilities.JwtUtil;
import com.server.utilities.Response;
import io.javalin.http.Handler;

public class Auth {
    public static Handler validate = ctx -> {
        String token = ctx.header("Authorization");
        if (token == null) {
            ctx.json(Response.UN_AUTHORIZED);
            return;
        }
        if(JwtUtil.verifyToken(token) == null){
            ctx.json(Response.UN_AUTHORIZED);
        }
        ctx.attribute("id", JwtUtil.getUserIdFromToken(token));
    };
}
