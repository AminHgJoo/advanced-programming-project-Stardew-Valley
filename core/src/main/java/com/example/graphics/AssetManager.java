package com.example.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class AssetManager {
    private static final HashMap<String, Texture> textures = new HashMap<>();
    private static Skin skin = null;

    public static void loadAssets() {
        //TODO:
        loadSkin();
        textures.put("launcherBackground", new Texture("images/launcher_background.png"));
    }

    private static void loadSkin() {
        skin = new Skin(Gdx.files.internal("skin/freezing/skin/freezing-ui.json"));
    }

    public static Skin getSkin() {
        return skin;
    }

    public static HashMap<String, Texture> getTextures() {
        return textures;
    }

    public static void disposeAssets() {
        for (String key : textures.keySet()) {
            if (textures.get(key) != null) {
                textures.get(key).dispose();
            }
        }
    }
}
