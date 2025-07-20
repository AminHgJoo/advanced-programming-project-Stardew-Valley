package com.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.client.utils.AssetManager;
import com.client.utils.HTTPUtil;
import com.client.utils.ModelDecoder;
import com.client.utils.MyScreen;
import com.client.views.inGameMenus.CookingMenu;
import com.client.views.inGameMenus.CraftingMenu;
import com.client.views.inGameMenus.StoreInterface;
import com.client.views.preGameMenus.LauncherMenu;
import com.client.views.preGameMenus.MainMenu;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import io.github.cdimascio.dotenv.Dotenv;

public class GameMain extends Game {
    public Music music = null;

    @Override
    public void create() {
        //TODO: load/play music.
//        loadEnv();
        AssetManager.loadAssets();
        //TODO: TESTING CODE FOR MAP :::::
//        this.setScreen(new FarmMenu(this));
//        this.setScreen(new FishingMiniGame(this, null, true, Quality.IRIDIUM));
//        this.setScreen(new StoreInterface(this, "Pierre's General Store", null));
        this.setScreen(new CookingMenu(this, null));
//
//        if (ClientApp.loggedInUser != null) {
//            ClientApp.init();
//            this.setScreen(new MainMenu(this));
//        } else
//            this.setScreen(new LauncherMenu(this));
    }

    public void loadEnv() {
        Dotenv.configure()
            .directory(System.getenv("address"))
            .filename("env.prod")
            .systemProperties()
            .load();
        if (System.getProperty("TOKEN") != null) {
            loadUser();
        }
    }

    public void loadUser() {
        String token = System.getProperty("TOKEN");
        ClientApp.token = token;
        try {
            var getResponse = HTTPUtil.get("http://localhost:8080/api/user/whoAmI");
            if (getResponse != null) {
                var res = HTTPUtil.deserializeHttpResponse(getResponse);
                if (res.getStatus() == 200) {
                    LinkedTreeMap map = (LinkedTreeMap) res.getBody();
                    Gson gson = new Gson();
                    String json = gson.toJson(map);
                    ClientApp.loggedInUser = ModelDecoder.decodeUser(json);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        if (music != null) {
            music.dispose();
        }

        if (screen != null) {
            screen.dispose();
        }

        if (AssetManager.getSkin() != null) {
            AssetManager.getSkin().dispose();
        }

        AssetManager.disposeAssets();

        try {
            ClientApp.client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        ClientApp.currentScreen = (MyScreen) screen;
    }
}
