package com.server.controllers.InGameControllers;

import com.client.utils.StringUtils;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.lang.reflect.Method;

public class GameServerController {
    public void routingTheRequests(Context ctx , GameServer gs) {
        String controllerName = ctx.pathParam("controllerName");
        String methodName = StringUtils.removeFirstCamelCaseWord(controllerName);
        if(methodName.isEmpty()){
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid method name"));
            return;
        }
        try {
            if (controllerName.contains("world")) {
                Method m = WorldController.class.getDeclaredMethod(methodName);
                m.invoke(ctx , gs);
            } else if (controllerName.contains("trading")) {
                Method m = TradingController.class.getDeclaredMethod(methodName);
                m.invoke(ctx , gs);
            } else if (controllerName.contains("npc")) {
                Method m = NpcController.class.getDeclaredMethod(methodName);
                m.invoke(ctx , gs);
            } else if (controllerName.contains("movement")) {
                Method m = MovementController.class.getDeclaredMethod(methodName);
                m.invoke(ctx , gs);
            } else if (controllerName.contains("loadSave")) {
                Method m = LoadSaveController.class.getDeclaredMethod(methodName);
                m.invoke(ctx , gs);
            } else if (controllerName.contains("liveStock")) {
                Method m = LiveStockController.class.getDeclaredMethod(methodName);
                m.invoke(ctx , gs);
            } else if (controllerName.contains("inventory")) {
                Method m = InventoryController.class.getDeclaredMethod(methodName);
                m.invoke(ctx , gs);
            } else if (controllerName.contains("friendship")) {
                Method m = FriendshipController.class.getDeclaredMethod(methodName);
                m.invoke(ctx , gs);
            } else if (controllerName.contains("farming")) {
                Method m = FarmingController.class.getDeclaredMethod(methodName);
                m.invoke(ctx , gs);
            } else if (controllerName.contains("dealing")) {
                Method m = DealingController.class.getDeclaredMethod(methodName);
                m.invoke(ctx , gs);
            } else if (controllerName.contains("cooking")) {
                Method m = CookingController.class.getDeclaredMethod(methodName);
                m.invoke(ctx , gs);
            } else if (controllerName.contains("artisan")) {
                Method m = ArtisanController.class.getDeclaredMethod(methodName);
                m.invoke(ctx , gs);
            }else {
                ctx.json(Response.NOT_FOUND.setMessage("No Controller found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
