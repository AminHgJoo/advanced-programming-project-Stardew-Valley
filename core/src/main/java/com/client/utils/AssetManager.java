package com.client.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class AssetManager {
    private static final HashMap<String, Texture> textures = new HashMap<>();
    private static Skin skin = null;
    private static BitmapFont font;
    private static Skin skin2;

    public static void loadAssets() {
        loadSkin();
        textures.put("launcherBackground", new Texture("images/launcher_background.png"));
        textures.put("profileBackground", new Texture("images/profile_background.png"));
        textures.put("mainMenuBackground", new Texture("images/mainMenu_background.jpg"));
        //TODO value of source is image
        loadTexturesRecursively(Gdx.files.internal(System.getenv("source")));
    }

    private static void loadTexturesRecursively(FileHandle directory) {
        for (FileHandle file : directory.list()) {
            if (file.isDirectory()) {
                loadTexturesRecursively(file);
            } else {
                String extension = file.extension().toLowerCase();
                if (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg")) {
                    String key = generateKeyFromFileName(file.nameWithoutExtension());

                    if (!textures.containsKey(key))
                        textures.put(key, new Texture(file));
                }
            }
        }
    }

    public static String generateKeyFromFileName(String fileName) {
        String[] parts = fileName.split(" ");
        if (fileName.contains("_"))
             parts = fileName.split("_");

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
                sb.append(parts[i].toLowerCase());

        }
        return sb.toString();
    }

    private static void loadSkin() {
        skin = new Skin(Gdx.files.internal("skin/freezing/skin/freezing-ui.json"));
        loadFont();
        skin.add("myFont", font);
        skin2 = new Skin(Gdx.files.internal("skin.clean-crispy/skin/clean-crispy-ui.json"));
        skin2.add("myFont", font);
    }

    public static Skin getSkin() {
        return skin;
    }

    public static Skin getSkin2() {
        return skin2;
    }

    public static HashMap<String, Texture> getTextures() {
        return textures;
    }

    public static Texture getImage(String name) {
        return textures.get(name.toLowerCase());
    }

    public static void disposeAssets() {
        for (String key : textures.keySet()) {
            if (textures.get(key) != null) {
                textures.get(key).dispose();
            }
        }

        if (skin != null) {
            skin.dispose();
        }
    }

    public static BitmapFont getFont() {
        return font;
    }

    private static void loadFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("skin/freezing/skin/Racingoftendemo-9M3nL.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        font = generator.generateFont(parameter);
        generator.dispose();
    }
}
