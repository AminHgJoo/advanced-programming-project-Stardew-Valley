package com.client.views.inGameMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.client.ClientApp;
import com.client.GameMain;
import com.client.utils.AssetManager;
import com.client.utils.HTTPUtil;
import com.client.utils.MyScreen;
import com.client.utils.UIPopupHelper;
import com.common.utils.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.server.utilities.Response;
import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatScreen implements MyScreen {
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;

    private final GameMain gameMain;
    private final FarmMenu farmMenu;

    private Table rootTable;
    private Table messagesTable;
    private ScrollPane scrollPane;
    private TextField inputField;

    private ArrayList<ChatMessage> chatMessages = new ArrayList<>();

    private enum CommandPrefixes {
        EXIT("^/exit$"),
        WHISPER("^/whisper\\s+-m\\s+(?<message>.*?)\\s+-r\\s+(?<recipient>.*?)$"),
        ;

        private final String regex;

        public boolean matches(String command) {
            return command.matches(regex);
        }

        public String getGroup(String command, String groupName) {
            Matcher matcher = Pattern.compile(regex).matcher(command);
            matcher.find();
            return matcher.group(groupName);
        }

        public static boolean isCommand(String command) {
            for (var commandPrefix : CommandPrefixes.values()) {
                if (commandPrefix.matches(command)) {
                    return true;
                }
            }
            return false;
        }

        CommandPrefixes(@Language("Regexp") String regex) {
            this.regex = regex;
        }
    }

    public ChatScreen(FarmMenu farmMenu, GameMain gameMain) {
        this.farmMenu = farmMenu;
        this.gameMain = gameMain;

        this.skin = AssetManager.getSkin();
        this.backgroundTexture = AssetManager.getImage("profileBackground");

        fetchMessages();

        initializeStage();
    }

    private void initializeStage() {
        stage = new Stage(new ScreenViewport());

        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.top();
        stage.addActor(rootTable);

        messagesTable = new Table();
        messagesTable.top().left();
        for (var chatMessage : chatMessages) {
            addMessage((chatMessage.sender.equals(ClientApp.loggedInUser.getUsername()) ? "You" : chatMessage.sender)
                + ": " + chatMessage.message, false);
        }

        scrollPane = new ScrollPane(messagesTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        inputField = new TextField("", skin);
        inputField.setMessageText("Type your message...");
        inputField.setTextFieldListener((textField, c) -> {
            if (c == '\n' || c == '\r') {
                String msg = textField.getText().trim();

                if (CommandPrefixes.isCommand(msg)) {
                    executeCommand(msg);
                } else if (!msg.isEmpty()) {
                    addMessage("You: " + msg, true);
                    textField.setText("");
                }
            }
        });

        rootTable.add(scrollPane)
            .expand().fill().pad(10)
            .row();
        rootTable.add(inputField)
            .expandX().fillX().pad(10).height(30);

        Gdx.input.setInputProcessor(stage);
    }

    private void addMessage(String text, boolean updateMessagesList) {
        Label msgLabel = new Label(text, skin);
        msgLabel.setWrap(true);
        messagesTable.add(msgLabel)
            .expandX().fillX().pad(2)
            .row();

        Gdx.app.postRunnable(() -> {
            scrollPane.layout();
            scrollPane.setScrollPercentY(1f);
        });

        if (updateMessagesList) {
            ChatMessage message = new ChatMessage(text, ClientApp.loggedInUser.getUsername(), null, false);
            chatMessages.add(message);
            //TODO: Send message to the server.
        }
    }

    private void executeCommand(String command) {
        if (CommandPrefixes.EXIT.matches(command)) {
            dispose();
            gameMain.setScreen(farmMenu);
        } else if (CommandPrefixes.WHISPER.matches(command)) {

        }
    }

    private void fetchMessages() {
        var req = new JsonObject();
        var postRes = HTTPUtil.post("http://localhost:8080/api/game/" + ClientApp.currentGameData.get_id()
            + "/chatFetchMessages", req);

        Response res = HTTPUtil.deserializeHttpResponse(postRes);

        if (res.getStatus() == 200) {
            Gson gson = new Gson();
            String msgArray = res.getBody().toString();
            ArrayList mediator = gson.fromJson(msgArray, ArrayList.class);

            for (var element : mediator) {
                LinkedTreeMap map = (LinkedTreeMap) element;
                String jsonData = map.toString();
                chatMessages.add(gson.fromJson(jsonData, ChatMessage.class));
            }
        } else {
            System.out.println("Error fetching chat messages");
        }
    }

    @Override
    public void socketMessage(String message) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.12f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
