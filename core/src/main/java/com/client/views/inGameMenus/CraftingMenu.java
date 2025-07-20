package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.common.models.Player;
import com.common.models.enums.recipes.CraftingRecipes;

public class CraftingMenu implements MyScreen {
    private final GameMain gameMain;
    private final FarmMenu farmMenu;

    private final Skin skin;
    private Stage stage;
    private final Texture background;

    public CraftingMenu(GameMain gameMain, FarmMenu farmMenu) {
        this.gameMain = gameMain;
        this.farmMenu = farmMenu;

        this.background = AssetManager.getImage("profilebackground");
        this.skin = AssetManager.getSkin();

        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        Image bg = new Image(background);
        stage.addActor(bg);
        bg.setFillParent(true);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Table rightTable = new Table(skin);



        Table leftTable = new Table();
        leftTable.pad(20);
        stage.addActor(leftTable);

        //TODO: Replace with ClientApp.currentPlayer
        Player player = new Player();

        CraftingRecipes[] allRecipes = CraftingRecipes.values();
        CraftingRecipes[][] descriptions = new CraftingRecipes[4][5];

        for (int i = 0; i < allRecipes.length; i++) {
            descriptions[i / 5][i % 5] = allRecipes[i];
        }

        for (int row = 0; row < descriptions.length; row++) {
            for (int col = 0; col < descriptions[row].length; col++) {

                CraftingRecipes recipes = descriptions[row][col];

                Image img = new Image(AssetManager.getImage(AssetManager.generateKeyFromFileName(recipes.name)));
                img.addListener(new TextTooltip(recipes.name + "\n" + recipes.description + "\n" + recipes.listIngredients(), skin));
                img.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {

                    }
                });

                if (!player.getUnlockedCraftingRecipes().contains(recipes)) {
                    img.setColor(0.6f, 0.6f, 0.6f, 0.6f);
                }
                leftTable.add(img).pad(10);
            }
            leftTable.row();
        }

// Add your widgets to leftTable and rightTable here
// e.g. leftTable.add(button1).row(); rightTable.add(label).row();

// Split evenly: expandX + fillX on each cell
        root.add(leftTable).expandX().fillX();
        root.add(rightTable).expandX().fillX();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void socketMessage(String message) {

    }

    @Override
    public void show() {
        TooltipManager manager = TooltipManager.getInstance();
        manager.initialTime = 0f;
        manager.subsequentTime = 0f;
        manager.hideAll();
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
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
    public void dispose() {
        stage.dispose();
    }
}
