package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;
import com.common.models.Backpack;
import com.common.models.Slot;
import com.server.controllers_old.gameMenuControllers.ArtisanController;

import java.util.ArrayList;
import java.util.List;

public class ArtisanMenu implements MyScreen, InputProcessor {
    private SpriteBatch batch;
    private final GameMain gameMain;
    private Backpack backpack;
    private ArrayList<Slot> targetSlots = new ArrayList<>();
    private Texture backgroundTexture;
    private Texture inventoryTexture;
    private final int ITEMS_PER_PAGE = 27;
    private int scrollIndex = 0;
    private MyScreen farmScreen;
    private int GRID_SIZE = 9;
    private int GRID_PADDING = 8;
    private int selectedIndex = -1;
    private int GRID_ITEM_SIZE = 95;
    private boolean selected = false;
    private int selectedSave = -1;
    private BitmapFont titleFont;
    private GlyphLayout layout;
    private String artisanName;
    private float startBtnX;
    private float startBtnY;
    private float startBtnWidth;
    private float startBtnHeight;
    private Stage stage;
    private Skin skin;
    private TextButton startButton;


    public ArtisanMenu(GameMain gameMain, MyScreen farmScreen, String artisanName) {
        this.gameMain = gameMain;
        this.batch = new SpriteBatch();
        backgroundTexture = AssetManager.getImage("profileBackground");
        inventoryTexture = AssetManager.getImage("inventory");
        this.farmScreen = farmScreen;
        backpack = ClientApp.currentPlayer.getInventory();
        findSlots(artisanName);
        titleFont = AssetManager.getStardewFont();
        titleFont.getData().setScale(3f);
        titleFont.setColor(Color.WHITE);
        this.layout = new GlyphLayout();
        this.artisanName = artisanName;
    }

    private void findSlots(String artisanName) {
        if (artisanName.equals("Bee House")) {
            for (Slot slot : backpack.getSlots()) {
                if (slot.getItem().getName().equals("Honey")) {
                    targetSlots.add(slot);
                }
            }
        } else if (artisanName.equals("Cheese Press")) {
            for (Slot slot : backpack.getSlots()) {
                if (ArtisanController.isCheesePress(slot.getItem().getName())) {
                    targetSlots.add(slot);
                }
            }
        } else if (artisanName.equals("Keg")) {
            for (Slot slot : backpack.getSlots()) {
                if (ArtisanController.isKeg(slot.getItem().getName())) {
                    targetSlots.add(slot);
                }
            }
        } else if (artisanName.equals("Dehydrator")) {
            for (Slot slot : backpack.getSlots()) {
                if (ArtisanController.isDehydrator(slot.getItem().getName())) {
                    targetSlots.add(slot);
                }
            }
        } else if (artisanName.equals("Charcoal Klin")) {
            for (Slot slot : backpack.getSlots()) {
                if (ArtisanController.isCharCoalKiln(slot.getItem().getName())) {
                    targetSlots.add(slot);
                }
            }
        } else if (artisanName.equals("Loom")) {
            for (Slot slot : backpack.getSlots()) {
                if (ArtisanController.isLoom(slot.getItem().getName())) {
                    targetSlots.add(slot);
                }
            }
        } else if (artisanName.equals("Mayonnaise Machine")) {
            for (Slot slot : backpack.getSlots()) {
                if (ArtisanController.isMayonnaiseMachine(slot.getItem().getName())) {
                    targetSlots.add(slot);
                }
            }
        } else if (artisanName.equals("Oil Maker")) {
            for (Slot slot : backpack.getSlots()) {
                if (ArtisanController.isOilMaker(slot.getItem().getName())) {
                    targetSlots.add(slot);
                }
            }
        } else if (artisanName.equals("Preserves Jar")) {
            for (Slot slot : backpack.getSlots()) {
                if (ArtisanController.isPreservesJar(slot.getItem().getName())) {
                    targetSlots.add(slot);
                }
            }
        } else if (artisanName.equals("Fish Smoker")) {
            for (Slot slot : backpack.getSlots()) {
                if (ArtisanController.isFish(slot.getItem().getName())) {
                    targetSlots.add(slot);
                }
            }
        }
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE) {
            gameMain.setScreen(farmScreen);
            this.dispose();
        } else if (i == Input.Keys.RIGHT) {
            gameMain.setScreen(new SkillMenu(gameMain, farmScreen));
        }
        return false;
    }


    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int startX = (Gdx.graphics.getWidth() / 2 - inventoryTexture.getWidth() / 2)
            + GRID_PADDING + 45;
        int startY = Gdx.graphics.getHeight() / 2 + inventoryTexture.getHeight() / 2
            + GRID_PADDING - 160;


        screenY = Gdx.graphics.getHeight() - screenY;

        for (int i = 0; i < ITEMS_PER_PAGE; i++) {
            int actualIndex = scrollIndex * GRID_SIZE + i;
            if (actualIndex >= backpack.getSlots().size()) break;

            int col = i % GRID_SIZE;
            int row = i / GRID_SIZE;
            int x = startX + col * (GRID_ITEM_SIZE + GRID_PADDING);
            int y = startY - row * (GRID_ITEM_SIZE + GRID_PADDING);

            if (screenX >= x && screenX <= x + GRID_ITEM_SIZE &&
                screenY >= y && screenY <= y + GRID_ITEM_SIZE) {

                if (selectedIndex >= 0 && selectedIndex == actualIndex) {
                    selectedIndex = -1;
                } else {
                    selectedIndex = actualIndex;
                }
                break;
            }
        }

        return true;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        int totalSlots = targetSlots.size();

        int maxRows = Math.max(0, (totalSlots - ITEMS_PER_PAGE + GRID_SIZE - 1) / GRID_SIZE);
        scrollIndex += (int) amountY;
        scrollIndex = Math.max(0, Math.min(scrollIndex, maxRows));
        return true;
    }

    @Override
    public void socketMessage(String message) {

    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = AssetManager.getSkin();

        startButton = new TextButton("Start", skin);
        startButton.setSize(200, 60);
        startButton.setPosition(Gdx.graphics.getWidth() / 2f - 100, 50);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO sooroosh komak
//            Request request = new Request("asghar");
//            request.body.put("itemName", targetSlots.get(selectedSave).getItem().getName());
//            request.body.put("artisanName", artisanName);
            }
        });

        stage.addActor(startButton);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        batch.draw(inventoryTexture,
            Gdx.graphics.getWidth() / 2 - inventoryTexture.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - inventoryTexture.getHeight() / 2,
            inventoryTexture.getWidth(), inventoryTexture.getHeight());

        String title = artisanName;
        layout.setText(titleFont, title);

        float xAsghar = Gdx.graphics.getWidth() / 2f - layout.width / 2f;
        float yAsghar = Gdx.graphics.getHeight() - 50f;
        titleFont.draw(batch, layout, xAsghar, yAsghar);

        int GRID_ITEM_SIZE = 95;
        int GRID_PADDING = 8;

        int startX = (Gdx.graphics.getWidth() / 2 - inventoryTexture.getWidth() / 2)
            + GRID_PADDING + 45;
        int startY = Gdx.graphics.getHeight() / 2 + inventoryTexture.getHeight() / 2
            + GRID_PADDING - 160;

        if (selectedIndex >= 0 && selected == false) {
            selected = true;
            selectedSave = selectedIndex;
            selectedIndex = -1;
        }
        for (int i = 0; i < ITEMS_PER_PAGE && i < backpack.getSlots().size(); i++) {
            int actualIndex = scrollIndex * GRID_SIZE + i;
            if (selectedIndex >= 0 && selected) {
                Slot temp = backpack.getSlots().get(selectedIndex);
                backpack.getSlots().set(selectedIndex, backpack.getSlots().get(selectedSave));
                backpack.getSlots().set(selectedSave, temp);
                selectedIndex = -1;
                selected = false;
                selectedSave = -1;
            }
        }

        List<Slot> slots = targetSlots;
        for (int i = 0; i < ITEMS_PER_PAGE && i < targetSlots.size(); i++) {
            int actualIndex = scrollIndex * GRID_SIZE + i;
            if (actualIndex >= slots.size()) break;

            int col = i % GRID_SIZE;
            int row = i / GRID_SIZE;

            int x = startX + col * (GRID_ITEM_SIZE + GRID_PADDING);
            int y = startY - row * (GRID_ITEM_SIZE + GRID_PADDING);


            if (actualIndex == selectedSave) {
                batch.setColor(1, 1, 0, 1);
                batch.draw(AssetManager.getImage("red"), x, y, GRID_ITEM_SIZE, GRID_ITEM_SIZE);
                batch.setColor(1, 1, 1, 1);
            }


            Texture itemTexture = slots.get(actualIndex).getItem().getTexture();
            batch.draw(itemTexture, x, y, GRID_ITEM_SIZE, GRID_ITEM_SIZE);
        }

        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

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
        batch.dispose();
        stage.dispose();
    }
}
