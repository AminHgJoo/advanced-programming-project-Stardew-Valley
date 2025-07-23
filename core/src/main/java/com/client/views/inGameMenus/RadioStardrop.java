package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.MyScreen;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class RadioStardrop implements MyScreen {
    private final GameMain gameMain;
    private final FarmMenu farmScreen;
    private final Skin skin;
    private final Texture background;
    private Stage stage;

    private Label currentlyPlaying;

    private final ArrayList<String> loadedFilePaths = new ArrayList<>();

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
        Table leftTable = new Table(); //TODO: The online stuff (tune in to other player's stuff.)

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
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Music Files", "mp3");
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

    //TODO: Called whenever music is played on client side and is new. Should start a new thread.
    private void uploadMusicToServer(String absPath) {

    }

    @Override
    public void socketMessage(String message) {

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
