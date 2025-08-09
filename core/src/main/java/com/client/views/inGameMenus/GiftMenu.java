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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.HTTPUtil;
import com.client.utils.MyScreen;
import com.common.models.Backpack;
import com.common.models.Player;
import com.common.models.Slot;
import com.google.gson.JsonObject;
import com.server.utilities.Response;

import java.util.List;

public class GiftMenu implements MyScreen, InputProcessor {
    private final GameMain gameMain;
    private final int ITEMS_PER_PAGE = 27;
    private final Skin skin;
    private SpriteBatch batch;
    private Backpack backpack;
    private Player targetPlayer;
    private Player currentPlayer;
    private Texture backgroundTexture;
    private Texture inventoryTexture;
    private int scrollIndex = 0;
    private FarmMenu farmScreen;
    private int GRID_SIZE = 9;
    private int GRID_PADDING = 8;
    private int selectedIndex = -1;
    private int GRID_ITEM_SIZE = 95;
    private boolean selected = false;
    private int selectedSave = -1;
    private BitmapFont titleFont;
    private GlyphLayout layout;
    private Stage stage;
    private TextField countField;
    private TextButton sendButton;
    private TextButton exitButton;


    public GiftMenu(GameMain gameMain, FarmMenu farmScreen, Player targetPlayer) {
        this.gameMain = gameMain;
        this.batch = new SpriteBatch();
        backgroundTexture = AssetManager.getImage("profileBackground");
        inventoryTexture = AssetManager.getImage("inventory");
        this.farmScreen = farmScreen;
        backpack = ClientApp.currentPlayer.getInventory();
        this.skin = AssetManager.getSkin();
        titleFont = AssetManager.getStardewFont();
        titleFont.getData().setScale(3f);
        titleFont.setColor(Color.WHITE);
        this.targetPlayer = targetPlayer;
        this.layout = new GlyphLayout();
        currentPlayer = ClientApp.currentPlayer;
        stage = new Stage(new ScreenViewport());

        countField = new TextField("", skin);
        countField.setMessageText("Enter count...");
        countField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return Character.isDigit(c);
            }
        });


        float fieldWidth = 200f;
        float fieldHeight = 50f;
        float xField = Gdx.graphics.getWidth() - fieldWidth - 50f;
        float yField = Gdx.graphics.getHeight() / 2f;

        countField.setSize(fieldWidth, fieldHeight);
        countField.setPosition(xField, yField);
        stage.addActor(countField);


        sendButton = new TextButton("Send", skin);
        sendButton.setSize(fieldWidth, fieldHeight);
        sendButton.setPosition(xField, yField - fieldHeight - 10f);

        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (countField.getText() != null) {
                    try {
                        int count = Integer.parseInt(countField.getText());
                        send(count);
                    } catch (NumberFormatException e) {
                        Gdx.app.log("GiftMenu", "Invalid integer input.");
                    }
                }
            }
        });

        exitButton = new TextButton("Exit", skin);
        exitButton.setSize(fieldWidth, fieldHeight);
        exitButton.setPosition(xField, yField - 2* fieldHeight - 20f);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameMain.setScreen(farmScreen);
                GiftMenu.this.dispose();
            }
        });


        stage.addActor(sendButton);
        stage.addActor(exitButton);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
    }

    //TODO receive gift

    public void send(int count) {
        if (selectedSave >= 0) {
            Slot slot = backpack.getSlots().get(selectedSave);
            JsonObject req = new JsonObject();
            req.addProperty("username", targetPlayer.getUser().getUsername());
            req.addProperty("item", slot.getItem().getName());
            req.addProperty("amount", count);
            var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id() + "/friendshipGift", req);
            Response res = HTTPUtil.deserializeHttpResponse(postResponse);
            if (res.getStatus() == 200) {
                selected = false;
                selectedSave = -1;
                selectedIndex = -1;
                String game = res.getBody().toString();
                ((FarmMenu) farmScreen).getPlayerController().updateGame(game);
            }

        }
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE) {
            gameMain.setScreen(farmScreen);
            this.dispose();
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
        int totalSlots = backpack.getSlots().size();

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

        String title = "Gift";
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
        List<Slot> slots = backpack.getSlots();
        for (int i = 0; i < ITEMS_PER_PAGE && i < backpack.getSlots().size(); i++) {
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
        titleFont.getData().setScale(1);
        batch.dispose();
        stage.dispose();
    }
}
