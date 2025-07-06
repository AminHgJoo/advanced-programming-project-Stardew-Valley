package com.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.client.utils.AssetManager;
import com.client.utils.HTTPUtil;
import com.client.utils.UserDecoder;
import com.client.views.LauncherMenu;
import com.client.views.MainMenu;
import com.common.models.User;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.server.utilities.Response;
import io.github.cdimascio.dotenv.Dotenv;

public class GameMain extends Game {
    public Music music = null;

    @Override
    public void create() {
        //TODO: load/play music.
        loadEnv();
        AssetManager.loadAssets();
        if (ClientApp.loggedInUser != null) {
            ClientApp.init();
            this.setScreen(new MainMenu(this));
        } else
            this.setScreen(new LauncherMenu(this));
    }

    public void loadEnv() {
        Dotenv.configure()
            .directory("../core/src/main/java/com/client/env")
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
                System.out.println(res.getBody().toString());
                if (res.getStatus() == 200) {
                    LinkedTreeMap map = (LinkedTreeMap) res.getBody();
                    Gson gson = new Gson();
                    String json = gson.toJson(map);
                    ClientApp.loggedInUser = UserDecoder.decode(json);
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
        //TODO: Dispose stuff and screen, this is a prototype currently.
        if (screen != null) {
            screen.dispose();
        }
        if (AssetManager.getSkin() != null) {
            AssetManager.getSkin().dispose();
        }
        AssetManager.disposeAssets();
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        //TODO: Set the static field "currMenu" in app, if needed.
    }
}
