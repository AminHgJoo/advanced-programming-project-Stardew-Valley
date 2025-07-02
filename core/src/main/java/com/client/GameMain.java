package com.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.client.utils.AssetManager;
import com.client.views.LauncherMenu;

public class GameMain extends Game {
    public Music music = null;

    @Override
    public void create() {
        //TODO: load/play music.
        AssetManager.loadAssets();
        this.setScreen(new LauncherMenu(this));
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
