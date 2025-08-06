package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.HTTPUtil;
import com.client.utils.MyScreen;
import com.client.utils.UIPopupHelper;
import com.common.models.Backpack;
import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.Slot;
import com.common.models.enums.recipes.CraftingRecipes;
import com.google.gson.JsonObject;

public class CraftingMenu implements MyScreen {
    private final GameMain gameMain;
    private final FarmMenu farmMenu;

    private final Skin skin;
    private final Texture background;
    private Stage stage;
    private Label selectedRecipeLabel;
    private CraftingRecipes selectedRecipe;

    public CraftingMenu(GameMain gameMain, FarmMenu farmMenu) {
        this.gameMain = gameMain;
        this.farmMenu = farmMenu;

        this.background = AssetManager.getImage("profilebackground");
        this.skin = AssetManager.getSkin();

        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        selectedRecipeLabel = new Label("Selected Recipe Will Show Up Here!", skin);

        Image bg = new Image(background);
        stage.addActor(bg);
        bg.setFillParent(true);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Table rightTable = new Table();
        Table leftTable = new Table();
        leftTable.pad(20);

        Player player = ClientApp.currentPlayer;

        CraftingRecipes[] allRecipes = CraftingRecipes.values();
        CraftingRecipes[][] descriptions = new CraftingRecipes[4][5];

        for (int i = 0; i < allRecipes.length; i++) {
            descriptions[i / 5][i % 5] = allRecipes[i];
        }

        for (int row = 0; row < descriptions.length; row++) {
            for (int col = 0; col < descriptions[row].length; col++) {

                CraftingRecipes recipes = descriptions[row][col];

                Image img = new Image(AssetManager.getImage(AssetManager.generateKeyFromFileName(recipes.name)));

                if (!player.getUnlockedCraftingRecipes().contains(recipes)) {
                    img.setColor(0.6f, 0.6f, 0.6f, 0.6f);
                }

                var listener = new TextTooltip(recipes.name + "\n" + recipes.description + "\n" + recipes.listIngredients(), skin) {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        boolean res = super.touchDown(event, x, y, pointer, button);
                        if (player.getUnlockedCraftingRecipes().contains(recipes)) {
                            selectedRecipe = recipes;
                            selectedRecipeLabel.setText("Selected Recipe: " + selectedRecipe.name);
                        }
                        return res;
                    }
                };
                img.addListener(listener);

                leftTable.add(img).pad(10);
            }
            leftTable.row();
        }

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                gameMain.setScreen(farmMenu);
            }
        });

        TextButton craftButton = new TextButton("Craft", skin);
        craftButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedRecipe == null) {
                    return;
                }

                String result = localController(selectedRecipe);
                if ("OK".equals(result)) {
                    notifyServerOfSuccessfulCrafting(selectedRecipe);

                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog("Crafting Successful!", "Success");
                } else {
                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog(result, "Failed");
                }
            }
        });

        rightTable.add(selectedRecipeLabel).pad(10).row();
        rightTable.row().row();
        rightTable.add(craftButton).pad(10).row();
        rightTable.add(backButton).pad(10).row();

        root.add(leftTable).expandX().fillX();
        root.add(rightTable).expandX().fillX();

        Gdx.input.setInputProcessor(stage);
    }

    private void notifyServerOfSuccessfulCrafting(CraftingRecipes recipe) {
        var req = new JsonObject();
        req.addProperty("recipe", recipe.name);

        var postRes = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id()
            + "/inventoryHandleCrafting", req);

        var res = HTTPUtil.deserializeHttpResponse(postRes);

        if (res.getStatus() == 200) {
            String playerJson = res.getBody().toString();
            farmMenu.getPlayerController().updatePlayerObject(playerJson);
        } else {
            UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
            uiPopupHelper.showDialog("Connection to server failed.", "Error");
        }
    }

    private String localController(CraftingRecipes targetRecipe) {

        GameData gameData = ClientApp.currentGameData;
        Player player = gameData.getCurrentPlayer();

        Backpack backpack = player.getInventory();

        if (backpack.getSlots().size() == backpack.getType().getMaxCapacity()) {
            return "Your backpack is full, you cannot craft any items!";
        }

        for (Slot ingredient : targetRecipe.ingredients) {
            Slot playerSlot = backpack.getSlotByItemName(ingredient.getItem().getName());

            if (playerSlot == null) {
                return "You don't have the required materials to craft this item!";
            }

            if (playerSlot.getCount() < ingredient.getCount()) {
                return "You don't have the required materials to craft this item!";
            }
        }
        return "OK";
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
