package com.example.graphics;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class AssetManager {
    private static final HashMap<String, Texture> textures = new HashMap<>();

    public static void loadAssets() {
        //TODO:
    }

    public static HashMap<String, Texture> getTextures() {
        return textures;
    }
}
