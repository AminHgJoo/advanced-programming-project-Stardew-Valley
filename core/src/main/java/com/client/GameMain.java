package com.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.client.utils.AssetManager;
import com.client.utils.HTTPUtil;
import com.client.utils.ModelDecoder;
import com.client.utils.MyScreen;
import com.client.views.preGameMenus.LauncherMenu;
import com.client.views.preGameMenus.MainMenu;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileOutputStream;

public class GameMain extends Game {
    public Music music = null;
    public String playingMusicName = "";

    @Override
    public void create() {

        loadEnv();
        AssetManager.loadAssets();

        if (ClientApp.loggedInUser != null) {
            this.setScreen(new MainMenu(this));
        } else
            this.setScreen(new LauncherMenu(this));
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
            var getResponse = HTTPUtil.get("/api/user/whoAmI");
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

        try (FileOutputStream fos = new FileOutputStream(System.getenv("address") + "/music.txt")) {
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
