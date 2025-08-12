package com.server.controllers.InGameControllers;

import com.client.utils.StringUtils;
import com.server.GameServers.GameServer;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.lang.reflect.Method;

public class GameServerController {
    private final WorldController worldController;
    private final TradingController tradingController;
    private final NpcController npcController;
    private final MovementController movementController;
    private final LoadSaveController loadSaveController;
    private final LiveStockController liveStockController;
    private final InventoryController inventoryController;
    private final FriendshipController friendshipController;
    private final FarmingController farmingController;
    private final DealingController dealingController;
    private final CookingController cookingController;
    private final ArtisanController artisanController;
    private final ChatController chatController;

    public GameServerController(GameServer gs) {
        worldController = new WorldController(gs);
        tradingController = new TradingController(gs);
        npcController = new NpcController(gs);
        movementController = new MovementController(gs);
        loadSaveController = new LoadSaveController(gs);
        liveStockController = new LiveStockController(gs);
        inventoryController = new InventoryController(gs);
        friendshipController = new FriendshipController(gs);
        farmingController = new FarmingController(gs);
        dealingController = new DealingController(gs);
        cookingController = new CookingController(gs);
        chatController = new ChatController(gs);
        artisanController = new ArtisanController(gs);
    }

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
            } else if (controllerName.contains("chat")) {
                Method m = ChatController.class.getDeclaredMethod(methodName, Context.class, GameServer.class);
                m.invoke(chatController, ctx, gs);
            } else {
                ctx.json(Response.NOT_FOUND.setMessage("No Controller found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}
