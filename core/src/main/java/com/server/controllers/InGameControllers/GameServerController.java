package com.server.controllers.InGameControllers;

import com.client.utils.StringUtils;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.lang.reflect.Method;

public class GameServerController {
    private final WorldController worldController = new WorldController();
    private final TradingController tradingController = new TradingController();
    private final NpcController npcController = new NpcController();
    private final MovementController movementController = new MovementController();
    private final LoadSaveController loadSaveController = new LoadSaveController();
    private final LiveStockController liveStockController = new LiveStockController();
    private final InventoryController inventoryController = new InventoryController();
    private final FriendshipController friendshipController = new FriendshipController();
    private final FarmingController farmingController = new FarmingController();
    private final DealingController dealingController = new DealingController();
    private final CookingController cookingController = new CookingController();
    private final ArtisanController artisanController = new ArtisanController();
    private final LeaderboardController leaderboardController = new LeaderboardController();

    public void routingTheRequests(Context ctx, GameServer gs) {
        String controllerName = ctx.pathParam("controllerName");
        String methodName = StringUtils.removeFirstCamelCaseWord(controllerName);
        if (methodName.isEmpty()) {
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid method name"));
            return;
        }
        try {
            if (controllerName.contains("world")) {
                Method m = WorldController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(worldController, ctx, gs);
            } else if (controllerName.contains("trading")) {
                Method m = TradingController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(tradingController, ctx, gs);
            } else if (controllerName.contains("npc")) {
                Method m = NpcController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(npcController, ctx, gs);
            } else if (controllerName.contains("movement")) {
                Method m = MovementController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(movementController, ctx, gs);
            } else if (controllerName.contains("loadSave")) {
                Method m = LoadSaveController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(loadSaveController, ctx, gs);
            } else if (controllerName.contains("liveStock")) {
                Method m = LiveStockController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(liveStockController, ctx, gs);
            } else if (controllerName.contains("inventory")) {
                Method m = InventoryController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(inventoryController, ctx, gs);
            } else if (controllerName.contains("friendship")) {
                Method m = FriendshipController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(friendshipController, ctx, gs);
            } else if (controllerName.contains("farming")) {
                Method m = FarmingController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(farmingController, ctx, gs);
            } else if (controllerName.contains("dealing")) {
                Method m = DealingController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(dealingController, ctx, gs);
            } else if (controllerName.contains("cooking")) {
                Method m = CookingController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(cookingController, ctx, gs);
            } else if (controllerName.contains("artisan")) {
                Method m = ArtisanController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(artisanController, ctx, gs);
            } else if (controllerName.contains("leaderboard")) {
                Method m = LeaderboardController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(leaderboardController, ctx, gs);
            } else {
                ctx.json(Response.NOT_FOUND.setMessage("No Controller found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
