package com.server.controllers.InGameControllers;

import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;

public class FriendshipController extends Controller {
    public void gift(Context ctx , GameServer gs){
        try{

        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
