package com.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.example.graphics.AssetManager;
import com.example.graphics.LauncherMenu;

import java.util.Objects;

public class GameMain extends Game {
    public Music music = null;

    @Override
    public void create() {
        //TODO:
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
    }

    @Override
    public void setScreen(Screen  screen) {
        super.setScreen(screen);
        //TODO: Set the static field "currMenu" in app, if needed.
    }
}
