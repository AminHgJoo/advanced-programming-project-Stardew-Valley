package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.common.models.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RadioStardrop implements MyScreen {
    private final GameMain gameMain;
    private final FarmMenu farmScreen;
    private final Skin skin;
    private final Texture background;
    private final ArrayList<String> loadedFilePaths = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Stage stage;
    private Label currentlyPlaying;

    public RadioStardrop(GameMain gameMain, FarmMenu farmScreen) {
        this.gameMain = gameMain;
        this.farmScreen = farmScreen;

        this.skin = AssetManager.getSkin();

        this.background = AssetManager.getImage("profilebackground");

        initializeStage();
    }

    private void initializeStage() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = AssetManager.getStardewFont();
        labelStyle.fontColor = Color.GOLD;

        BitmapFont customFont = AssetManager.getStardewFont();

        TextButton.TextButtonStyle baseStyle = skin.get(TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle customStyle = new TextButton.TextButtonStyle();
        customStyle.up = baseStyle.up;
        customStyle.down = baseStyle.down;
        customStyle.checked = baseStyle.checked;
        customStyle.disabled = baseStyle.disabled;
        customStyle.font = customFont;
        customStyle.fontColor = baseStyle.fontColor;
        customStyle.pressedOffsetX = baseStyle.pressedOffsetX;
        customStyle.pressedOffsetY = baseStyle.pressedOffsetY;

        stage = new Stage(new ScreenViewport());

        Image image = new Image(background);
        image.setFillParent(true);
        stage.addActor(image);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Table rightTable = new Table(); //The local stuff (back, load song, pause/play, change volume, upload to server.)
        Table leftTable = new Table(); //The online stuff (tune in to other player's stuff.)

        if (currentlyPlaying == null) {
            currentlyPlaying = new Label("Currently Playing: Nothing", labelStyle);
        }
        currentlyPlaying.setColor(Color.GOLD);

        Slider volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        volumeSlider.setValue(0.2f);
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameMain.music != null) {
                    gameMain.music.setVolume(volumeSlider.getValue());
                }
            }
        });

        TextButton toggleMusicButton = new TextButton("Toggle Music", customStyle);
        toggleMusicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameMain.music != null) {
                    if (gameMain.music.isPlaying()) {
                        gameMain.music.getPosition();
                        gameMain.music.pause();
                    } else {
                        gameMain.music.play();
                    }
                }
            }
        });

        TextButton backButton = new TextButton("Back", customStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                gameMain.setScreen(farmScreen);
            }
        });

        TextButton loadMusicButton = new TextButton("Load Music", customStyle);
        loadMusicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                new Thread(() -> {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (ClassNotFoundException | InstantiationException
                             | IllegalAccessException | UnsupportedLookAndFeelException e) {
                        e.printStackTrace();
                    }

                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Music Files", "mp3", "ogg");
                    fileChooser.setFileFilter(filter);
                    int returnVal = fileChooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();

                        writeToFile(selectedFile.getAbsolutePath());

                        uploadMusicToServer(selectedFile.getAbsolutePath());

                        System.out.println("Selected file: " + selectedFile.getAbsolutePath());

                        if (gameMain.music != null) {
                            gameMain.music.stop();
                            gameMain.music.dispose();
                        }

                        gameMain.music = Gdx.audio.newMusic(Gdx.files.absolute(selectedFile.getAbsolutePath()));
                        gameMain.playingMusicName = selectedFile.getName();
                        gameMain.music.setLooping(true);
                        gameMain.music.setVolume(volumeSlider.getValue());
                        gameMain.music.play();

                        configureCurrentlyPlaying(selectedFile.getAbsolutePath());

                        Gdx.app.postRunnable(() -> {
                            dispose();
                            initializeStage();
                        });
                    }
                }).start();
            }
        });

        Label volumeLabel = new Label("Volume", labelStyle);
        volumeLabel.setColor(Color.GOLD);

        Label nameLabel = new Label("Radio Stardrop", labelStyle);
        nameLabel.setColor(Color.GOLD);

        Image stardrop = new Image(AssetManager.getImage("stardrop"));
        stardrop.setScale(2);

        Table paneTable = new Table();
        for (String musicPath : loadedFilePaths) {
            TextButton playButton = new TextButton(getMusicName(musicPath), customStyle);
            playButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {

                    if (gameMain.music != null) {
                        gameMain.music.stop();
                        gameMain.music.dispose();
                    }

                    gameMain.music = Gdx.audio.newMusic(Gdx.files.absolute(musicPath));
                    gameMain.playingMusicName = getFileName(musicPath);
                    gameMain.music.setLooping(true);
                    gameMain.music.setVolume(volumeSlider.getValue());
                    gameMain.music.play();

                    configureCurrentlyPlaying(musicPath);
                }
            });
            paneTable.add(playButton).row();
        }
        ScrollPane scrollPane = new ScrollPane(paneTable, skin);

        rightTable.center();
        rightTable.add(stardrop);
        rightTable.add(nameLabel).row();
        rightTable.add(currentlyPlaying).colspan(2).pad(10).row();
        rightTable.add(volumeLabel).colspan(2).pad(10).row();
        rightTable.add(volumeSlider).colspan(2).pad(10).row();
        rightTable.add(loadMusicButton).colspan(2).pad(10).row();
        rightTable.add(toggleMusicButton).colspan(2).pad(10).row();
        rightTable.add(backButton).colspan(2).pad(10).row();
        rightTable.add(scrollPane).colspan(2).pad(10).row();

        //----------Other Table Config----------//

        Label label = new Label("Tune to other players", labelStyle);
        label.setColor(Color.GOLD);

        Table tuneTable = new Table();

        for (Player player : ClientApp.currentGameData.getPlayers()) {
            if (player == ClientApp.currentPlayer) {
                continue;
            }

            tuneTable.add(new Label(player.getUser().getUsername(), labelStyle)).pad(10);

            TextButton tuneButton = new TextButton("Tune", customStyle);
            tuneButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    querySelectedPlayer(player);
                }
            });
            tuneTable.add(tuneButton).pad(10).row();
        }

        ScrollPane playersList = new ScrollPane(tuneTable, skin);

        leftTable.center();
        leftTable.add(label).row();
        leftTable.add(playersList).row();

        root.add(leftTable).expandX().fillX();
        root.add(rightTable).expandX().fillX();

        Gdx.input.setInputProcessor(stage);
    }

    private void configureCurrentlyPlaying(String absPath) {
        String musicName = getMusicName(absPath);
        currentlyPlaying.setText("Currently Playing: " + musicName);
    }

    private String getMusicName(String absPath) {
        String fileName = absPath.split("\\\\")[absPath.split("\\\\").length - 1];
        if (fileName.endsWith(".mp3")) {
            fileName = fileName.substring(0, fileName.length() - 4);
        }

        if (fileName.length() > 20) {
            fileName = fileName.substring(0, 20);
            fileName += "...";
        }

        return fileName;
    }

    private String getFileName(String absPath) {
        return absPath.split("\\\\")[absPath.split("\\\\").length - 1];
    }

    private void writeToFile(String text) {
        File file = new File(System.getenv("address") + "/music.txt");

        try (FileInputStream fis = new FileInputStream(file)) {
            String data = new String(fis.readAllBytes());
            fis.close();

            if (!data.contains(text)) {
                loadedFilePaths.add(text);

                try (FileOutputStream fos = new FileOutputStream(file, true)) {
                    fos.write(text.getBytes());
                    fos.write("\n".getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadMusicToServer(String absPath) {
        HTTPUtil.uploadFile(absPath, ClientApp.currentGameData.get_id());
    }

    private void downloadMusicFromServer(String musicName) {
        HTTPUtil.downloadFile(musicName, ClientApp.currentGameData.get_id());
    }

    @Override
    public void socketMessage(String message) {
        HashMap<String, String> res = (HashMap<String, String>) gson.fromJson(message, HashMap.class);
        String type = res.get("type");

        if (type.equals("MUSIC_QUERY")) {
            var req = new JsonObject();
            if (gameMain.music != null) {
                req.addProperty("isPlaying", gameMain.music.isPlaying());
                req.addProperty("musicName", gameMain.playingMusicName);
                req.addProperty("musicPos", (double) gameMain.music.getPosition());
            } else {
                req.addProperty("isPlaying", false);
                req.addProperty("musicName", "");
                req.addProperty("musicPos", 0.0);
            }

            var postRes = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id() + "/music/sync_res", req);

        } else if (type.equals("SYNC_DATA")) {
            String syncData = res.get("syncData");

            JsonObject syncDataJson = JsonParser.parseString(syncData).getAsJsonObject();

            String musicName = syncDataJson.get("musicName").getAsString();
            double pos = syncDataJson.get("musicPos").getAsDouble();
            boolean playingMusic = syncDataJson.get("isPlaying").getAsBoolean();

            if (!playingMusic) {
                UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
                uiPopupHelper.showDialog("Selected User Isn't Listening to Music Right Now.", "Error");
            } else {
                new Thread(() -> {
                    downloadMusicFromServer(musicName);

                    FileHandle fh = Gdx.files.internal("downloads/" + musicName);
                    File selectedFile = fh.file();

                    writeToFile(selectedFile.getAbsolutePath());

                    if (gameMain.music != null) {
                        gameMain.music.stop();
                        gameMain.music.dispose();
                    }

                    gameMain.music = Gdx.audio.newMusic(fh);
                    gameMain.playingMusicName = selectedFile.getName();
                    gameMain.music.setLooping(true);
                    gameMain.music.play();
                    System.out.println(pos);
                    gameMain.music.setPosition((float) pos);

                    configureCurrentlyPlaying(selectedFile.getAbsolutePath());

                    Gdx.app.postRunnable(() -> {
                        dispose();
                        initializeStage();
                    });
                }).start();
            }
        }
    }

    private void querySelectedPlayer(Player player) {
        var req = new JsonObject();
        req.addProperty("senderUsername", ClientApp.loggedInUser.getUsername());
        req.addProperty("syncTargetUsername", player.getUser().getUsername());

        var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id() + "/music/sync_req", req);

        var res = HTTPUtil.deserializeHttpResponse(postResponse);

        UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, skin);
        uiPopupHelper.showDialog(res.getMessage(), "Message");
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
