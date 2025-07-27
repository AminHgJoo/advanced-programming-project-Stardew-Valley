package com.client.views.preGameMenus.profileMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.server.views.gameViews.GameMenu;


public class AvatarMenu implements MyScreen {
    private Stage stage;
    private Skin skin;
    private Image[] avatarImages;
    private Texture[] avatarTextures;
    private TextButton selectFileButton, backButton;
    private int selectedIndex = -1;
    private Texture backgroundTexture, currentAvatarTexture;
    private Image backgroundImage, currentAvatarImage;
    private float checkTimer = 0;
    private static final float CHECK_INTERVAL = 0.5f;
    private GameMain gameMain;

    public AvatarMenu(GameMain gameMain) {
        this.gameMain = gameMain;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        skin = AssetManager.getSkin();
        backgroundTexture = AssetManager.getImage("profileBackground");
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        String[] avatarPaths = new String[1000];

        avatarTextures = new Texture[7];
        avatarImages = new Image[7];

        for (int i = 0; i < 7; i++) {
            avatarTextures[i] = new Texture(Gdx.files.internal("avatars/avatar" + (i + 1) + ".jpg"));
            avatarImages[i] = new Image(avatarTextures[i]);
            avatarPaths[i] = "avatars/avatar" + (i + 1) + ".jpg";
            final int index = i;
            avatarImages[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedIndex = index;
                    updateSelection();
                    ClientApp.avatarPath = avatarPaths[index];
                    currentAvatarTexture = avatarTextures[index];
                    currentAvatarImage.setDrawable(new TextureRegionDrawable(new TextureRegion(currentAvatarTexture)));
                }
            });
        }

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();

        Table avatarTable = new Table();
        avatarTable.center();
        for (Image avatar : avatarImages) {
            avatarTable.add(avatar).size(100, 100).pad(5);
        }


        backButton = new TextButton("back", skin);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.dispose();
                //TODO bug
                gameMain.setScreen(new ProfileMenu(gameMain));
            }
        });
        currentAvatarTexture = new Texture(Gdx.files.internal(ClientApp.avatarPath));
        currentAvatarImage = new Image(currentAvatarTexture);
        currentAvatarImage.setSize(150, 150);
        Table buttonTable = new Table();
        buttonTable.center();
        buttonTable.add(selectFileButton).size(500, 100).pad(10);
        buttonTable.row();
        buttonTable.add(backButton).size(500, 100).pad(10);

        mainTable.add(currentAvatarImage).size(150, 150).padBottom(20);
        mainTable.row();
        mainTable.add(avatarTable);
        mainTable.row();
        mainTable.add(buttonTable);

        stage.addActor(mainTable);

        Gdx.input.setInputProcessor(stage);
    }

    private void updateSelection() {
        for (int i = 0; i < avatarImages.length; i++) {
            avatarImages[i].setColor(i == selectedIndex ? Color.RED : Color.WHITE);
        }
    }

    @Override
    public void render(float delta) {
        checkTimer += delta;
        if (checkTimer >= CHECK_INTERVAL) {
            checkTimer = 0;
        }
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();
    }

    private boolean isValidImageFile(String path) {
        String[] validExtensions = {".png", ".jpg", ".jpeg"};
        String lowerPath = path.toLowerCase();
        for (String ext : validExtensions) {
            if (lowerPath.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void socketMessage(String message) {

    }
}
