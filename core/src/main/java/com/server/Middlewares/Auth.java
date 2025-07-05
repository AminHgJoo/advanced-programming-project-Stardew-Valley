package com.server.Middlewares;

import com.server.utilities.JwtUtil;
import com.server.utilities.Response;
import io.javalin.http.Handler;

public class Auth {
    public static Handler validate = ctx -> {
        String token = ctx.header("Authorization");
        if (token == null) {
            ctx.json(Response.UN_AUTHORIZED);
            throw new Exception();
        }
        if(JwtUtil.verifyToken(token) == null){
            ctx.json(Response.UN_AUTHORIZED);
            throw new Exception();
        }
        System.out.println(JwtUtil.getUserIdFromToken(token));
        ctx.attribute("id", JwtUtil.getUserIdFromToken(token));
    };
}
