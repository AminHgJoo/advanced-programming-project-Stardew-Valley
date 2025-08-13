package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.HTTPUtil;
import com.client.utils.MyScreen;
import com.client.utils.UIPopupHelper;
import com.common.models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class StoreInterface implements MyScreen {

    private final ArrayList<StoreProduct> storeProducts = new ArrayList<>();
    private final String storeName;
    private final GameMain gameMain;
    private final VillageMenu villageMenu;
    private final Skin skin;
    private final Texture background;
    private StoreProduct selectedItem = null;
    private int selectedAmount = 1;
    private Stage stage;
    private TextButton buyButton;

    public final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
            @Override
            public void write(JsonWriter out, LocalDateTime value) throws IOException {
                out.value(value.toString());
            }

            @Override
            public LocalDateTime read(JsonReader in) throws IOException {
                return LocalDateTime.parse(in.nextString());
            }
        })
        .serializeSpecialFloatingPointValues()
        .create();

    private boolean ignoreOutOfStockItems = false;

    public StoreInterface(GameMain gameMain, String storeName, VillageMenu villageMenu) {
        this.gameMain = gameMain;
        this.storeName = storeName;
        this.villageMenu = villageMenu;

        this.skin = AssetManager.getSkin();
        this.background = AssetManager.getImage("profilebackground");

        fetchStoreProducts(storeName);
        initializeStage();
    }

    private void fetchStoreProducts(String storeName) {
        GameData game = ClientApp.currentGameData;
        Store store = game.getMap().getVillage().getStore(storeName);
        if (store == null) return;
        //Test Only!
        storeProducts.clear();
//        StoreProduct product = new StoreProduct(AllProducts.ANCIENT_SEED, "Pierre's General Store");
//        product.setPrice(200);
//        product.setAvailableCount(0);
//        storeProducts.add(product);
//        storeProducts.add(new StoreProduct(AllProducts.BOUQUET, "Pierre's General Store"));
//
//        storeProducts.add(new StoreProduct(AllProducts.ANCIENT_SEED, "Pierre's General Store"));
//        storeProducts.add(new StoreProduct(AllProducts.BOUQUET, "Pierre's General Store"));
//
//        storeProducts.add(new StoreProduct(AllProducts.ANCIENT_SEED, "Pierre's General Store"));
//        storeProducts.add(new StoreProduct(AllProducts.BOUQUET, "Pierre's General Store"));
//
//        storeProducts.add(new StoreProduct(AllProducts.ANCIENT_SEED, "Pierre's General Store"));
//        storeProducts.add(new StoreProduct(AllProducts.BOUQUET, "Pierre's General Store"));
//
        //        storeProducts.add(new StoreProduct(AllProducts.ANCIENT_SEED, "Pierre's General Store"));
//        storeProducts.add(new StoreProduct(AllProducts.BOUQUET, "Pierre's General Store"));
        storeProducts.addAll(store.getAvailableProducts());
    }

    private void initializeStage() {
        Player player = ClientApp.currentPlayer;
        Backpack backpack = player.getInventory();
        Texture goldImage = AssetManager.getImage("gold");

        BitmapFont customFont = AssetManager.getStardewFont();

        final TextButton.TextButtonStyle baseStyle = skin.get(TextButton.TextButtonStyle.class);
        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = baseStyle.up;
        textButtonStyle.down = baseStyle.down;
        textButtonStyle.checked = baseStyle.checked;
        textButtonStyle.disabled = baseStyle.disabled;
        textButtonStyle.font = customFont;
        textButtonStyle.fontColor = baseStyle.fontColor;
        textButtonStyle.pressedOffsetX = baseStyle.pressedOffsetX;
        textButtonStyle.pressedOffsetY = baseStyle.pressedOffsetY;

        final Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.GOLD;
        labelStyle.font = AssetManager.getStardewFont();

        stage = new Stage(new ScreenViewport());

        Image bg = new Image(background);
        stage.addActor(bg);

        Table mainTable = new Table();
        mainTable.setPosition(0, 0);
        mainTable.setWidth(stage.getWidth());
        mainTable.setHeight(stage.getHeight());

        Table root = new Table();
        root.setHeight(800);
        root.setWidth(1500);
        root.center();

        for (StoreProduct storeProduct : storeProducts) {
            Label label = new Label(storeProduct.getType().getName() + "  " + storeProduct.getType().getPrice(), labelStyle);

            if (storeProduct.getAvailableCount() <= 0) {
                if (ignoreOutOfStockItems) {
                    continue;
                }

                label.setColor(Color.DARK_GRAY);
            }

            root.add(label);
            root.add(new Image(goldImage)).pad(10);
            Label label1 = new Label("Stock: " + (storeProduct.getAvailableCount() == Double.POSITIVE_INFINITY ? "Infinite" : String.format("%.0f", storeProduct.getAvailableCount())), labelStyle);

            if (storeProduct.getAvailableCount() <= 0) {
                label1.setColor(Color.DARK_GRAY);
            }

            root.add(label1).pad(10);

            if (storeProduct.getAvailableCount() > 0) {
                TextButton selectButton = new TextButton("Select", textButtonStyle);
                selectButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        selectedItem = storeProduct;
                        Gdx.app.postRunnable(() -> {
                            dispose();
                            initializeStage();
                        });
                    }
                });

                root.add(selectButton).pad(10);
            } else {
                Label label2 = new Label("Out Of Stock!", labelStyle);
                label2.setColor(Color.RED);
                root.add(label2).pad(10);
            }
            root.row();
        }

        ScrollPane scrollPane = new ScrollPane(root, skin);
        scrollPane.setHeight(800);
        scrollPane.setWidth(1500);

        Label inventoryLabel = new Label("Backpack: " + backpack.getSlots().size() + "/" + backpack.getType().getMaxCapacity(), labelStyle);
        Label moneyLabel = new Label("Gold: " + player.getMoney(ClientApp.currentGameData), labelStyle);
        Label productDescription = new Label(productDescription(), labelStyle);
        Label welcomeLabel = new Label("Welcome to " + storeName, labelStyle);
        Image npcPortrait = getNPCPortrait(storeName);

        TextButton backButton = new TextButton("Back", textButtonStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                gameMain.setScreen(villageMenu);
            }
        });

        TextButton plusButton = new TextButton("+", textButtonStyle);
        plusButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedItem != null) {
                    selectedAmount = (int) Math.min(selectedAmount + 1, selectedItem.getAvailableCount());
                    buyButton.setText("Buy " + selectedAmount);
                }
            }
        });

        TextButton minusButton = new TextButton("-", textButtonStyle);
        minusButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedItem != null) {
                    selectedAmount = Math.max(selectedAmount - 1, 1);
                    buyButton.setText("Buy " + selectedAmount);
                }
            }
        });

        buyButton = new TextButton("Buy " + (selectedItem == null ? "" : selectedAmount), textButtonStyle);
        buyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectedItem != null) {
                    buyItem();
                }
            }
        });

        TextButton filterButton = new TextButton("Filter Out Of Stock", textButtonStyle);
        filterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ignoreOutOfStockItems = !ignoreOutOfStockItems;
                Gdx.app.postRunnable(() -> {
                    dispose();
                    initializeStage();
                });
            }
        });

        mainTable.center();
        mainTable.add(inventoryLabel).align(Align.left).pad(10);
        mainTable.add(backButton).expandX();
        mainTable.add(moneyLabel).pad(10);
        mainTable.row();
        mainTable.add(npcPortrait).pad(10);
        mainTable.add(scrollPane).expandX().fillX().align(Align.left).pad(10);
        mainTable.add(productDescription).pad(10);
        mainTable.row();
        mainTable.add(welcomeLabel).align(Align.left).pad(10);
        mainTable.add(plusButton).align(Align.right).pad(10);
        mainTable.add(minusButton).align(Align.right).pad(10);
        mainTable.row();
        mainTable.add(buyButton).align(Align.right).colspan(3).pad(10);
        mainTable.add(filterButton).align(Align.right).pad(10);

        stage.addActor(mainTable);

        Gdx.input.setInputProcessor(stage);
    }

    private void buyItem() {
        if (selectedItem.getType() != null) {
            var req = new JsonObject();
            req.addProperty("productName", selectedItem.getType().getName());
            req.addProperty("storeName", storeName);
            req.addProperty("count", selectedAmount);

            var postRes = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id() +
                "/dealingPurchase", req);

            var res = HTTPUtil.deserializeHttpResponse(postRes);

            if (res.getStatus() == 200) {
                Gdx.app.postRunnable(() -> {
                    dispose();
                    selectedItem = null;
                    selectedAmount = 0;
                    initializeStage();
                    UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                    uiPopupHelper.showDialog("Success", "Success");
                });
            } else {
                System.out.println(res.getMessage());
                UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                uiPopupHelper.showDialog(res.getMessage(), "Error");
            }
        }
    }

    private String productDescription() {
        return selectedItem == null ? "No Item Selected." : (selectedItem.getType().getName() + ".\n" + selectedItem.getType().getDescription());
    }

    /**
     * @apiNote Make sure to correctly spell these names in the code!
     * @author AminHg
     */
    private Image getNPCPortrait(String storeName) {
        if (storeName.equals("Blacksmith")) {
            return new Image(AssetManager.getImage("clint"));
        } else if (storeName.equals("Carpenter's Shop")) {
            return new Image(AssetManager.getImage("robin"));
        } else if (storeName.equals("Fish Shop")) {
            return new Image(AssetManager.getImage("willy"));
        } else if (storeName.equals("JojaMart")) {
            return new Image(AssetManager.getImage("morris"));
        } else if (storeName.equals("Marnie's Ranch")) {
            return new Image(AssetManager.getImage("marnie"));
        } else if (storeName.equals("Pierre's General Store")) {
            return new Image(AssetManager.getImage("pierre"));
        } else if (storeName.equals("The Stardrop Saloon")) {
            return new Image(AssetManager.getImage("gus"));
        } else {
            return null;
        }
    }

    @Override
    public void socketMessage(String message) {
        HashMap<String, String> res = (HashMap<String, String>) gson.fromJson(message, HashMap.class);
        String type = res.get("type");

        if (type.equals("GAME_UPDATED")) {
            String game = res.get("game");
            villageMenu.getPlayerController().updateGame(game);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(() -> {
                        dispose();
                        initializeStage();
                    });
                }
            }, 2000);
        }
    }

    @Override
    public void show() {

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
