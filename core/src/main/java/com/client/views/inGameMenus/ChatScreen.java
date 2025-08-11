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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.server.utilities.Response;
import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatScreen implements MyScreen {
    private final GameMain gameMain;
    private final FarmMenu farmMenu;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Table rootTable;
    private Table messagesTable;
    private ScrollPane scrollPane;
    private TextField inputField;
    private ArrayList<ChatMessage> chatMessages = new ArrayList<>();

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
            addMessage(chatMessage.message, false, chatMessage.sender, chatMessage.recipient, chatMessage.isPrivate, false);
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
                    textField.setText("");
                } else if (!msg.isEmpty()) {
                    if (msg.startsWith("/")) {
                        parseCheatCode(msg);
                    } else {
                        addMessage(msg, true, ClientApp.loggedInUser.getUsername(), null, false, true);
                    }
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

    private String configureHostName(String name) {
        if (ClientApp.loggedInUser.getUsername().equals(name)) {
            return "You";
        }
        return name;
    }

    private void addMessage(String msgText, boolean updateMessagesData, String sender, String recipient, boolean isPrivate, boolean sendToServer) {
        String username = ClientApp.loggedInUser.getUsername();

        if (!isPrivate) {
            Label msgLabel = new Label(configureHostName(sender) + ": " + msgText, skin);
            msgLabel.setWrap(true);
            messagesTable.add(msgLabel)
                .expandX().fillX().pad(2)
                .row();

            Gdx.app.postRunnable(() -> {
                scrollPane.layout();
                scrollPane.setScrollPercentY(1f);
            });
        } else if (username.equals(sender) || username.equals(recipient)) {
            Label msgLabel = new Label("whisper from " + configureHostName(sender) + " to "
                + configureHostName(recipient) + ": " + msgText, skin);

            msgLabel.setWrap(true);
            messagesTable.add(msgLabel)
                .expandX().fillX().pad(2)
                .row();

            Gdx.app.postRunnable(() -> {
                scrollPane.layout();
                scrollPane.setScrollPercentY(1f);
            });
        }

        if (updateMessagesData) {
            ChatMessage message = new ChatMessage(msgText, sender, recipient, isPrivate);
            chatMessages.add(message);
            ClientApp.currentGameData.chatMessages.add(message);

            if (sendToServer) {
                sendMessageToServer(message);
            }
        }
    }

    private void sendMessageToServer(ChatMessage message) {
        var req = new JsonObject();
        req.addProperty("message", message.message);
        req.addProperty("sender", message.sender);
        req.addProperty("recipient", message.recipient);
        req.addProperty("isPrivate", message.isPrivate);

        var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id()
            + "/chatAddMessage", req);

        Response res = HTTPUtil.deserializeHttpResponse(postResponse);

        if (res.getStatus() == 200) {
            System.out.println("Khoda ro shokr");
        } else {
            UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, AssetManager.getSkin());
            uiPopupHelper.showDialog("Error connecting to server", "Error");
        }
    }

    private void executeCommand(String command) {
        if (CommandPrefixes.EXIT.matches(command)) {
            dispose();
            gameMain.setScreen(farmMenu);
        } else if (CommandPrefixes.WHISPER.matches(command)) {
            String sender = ClientApp.loggedInUser.getUsername();
            String recipient = CommandPrefixes.WHISPER.getGroup(command, "recipient");
            String message = CommandPrefixes.WHISPER.getGroup(command, "message");

            addMessage(message, true, sender, recipient, true, true);
        }
    }

    private void parseCheatCode(String cheatCode) {
        var req = new JsonObject();
        req.addProperty("command", cheatCode.substring(1));

        var postResponse = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id()
            + "/chatParseCheat", req);

        Response res = HTTPUtil.deserializeHttpResponse(postResponse);

        if (res.getStatus() == 200) {
            System.out.println("Khoda ro shokr. Cheat Kardim Raft!");
        } else {
            UIPopupHelper uiPopupHelper = new UIPopupHelper(stage, AssetManager.getSkin());
            uiPopupHelper.showDialog(res.getMessage(), "Error");
        }
    }

    private void fetchMessages() {
        var req = new JsonObject();
        var postRes = HTTPUtil.post("/api/game/" + ClientApp.currentGameData.get_id()
            + "/chatFetchMessages", req);

        Response res = HTTPUtil.deserializeHttpResponse(postRes);

        if (res.getStatus() == 200) {
            String msgArray = res.getBody().toString();
            ArrayList mediator = gson.fromJson(msgArray, ArrayList.class);

            for (var element : mediator) {
                LinkedTreeMap map = (LinkedTreeMap) element;
                String jsonData = map.toString();
                chatMessages.add(gson.fromJson(jsonData, ChatMessage.class));
            }

            ClientApp.currentGameData.chatMessages = chatMessages;
        } else {
            System.out.println("Error fetching chat messages");
        }
    }

    @Override
    public void socketMessage(String message) {
        HashMap<String, String> res = (HashMap<String, String>) gson.fromJson(message, HashMap.class);
        String type = res.get("type");

        if (type.equals("MESSAGE_ADDED")) {
            String messageJson = res.get("message");
            System.out.println(messageJson);
            ChatMessage chatMsg = gson.fromJson(messageJson, ChatMessage.class);
            addMessage(chatMsg.message, true, chatMsg.sender, chatMsg.recipient, chatMsg.isPrivate, false);
        } else if (type.equals("THOR")) {
            farmMenu.thorFlag = true;
        }
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

    private enum CommandPrefixes {
        EXIT("^/exit$"),
        WHISPER("^/whisper\\s+-m\\s+(?<message>.*?)\\s+-r\\s+(?<recipient>.*?)$"),
        ;

        private final String regex;

        CommandPrefixes(@Language("Regexp") String regex) {
            this.regex = regex;
        }

        public static boolean isCommand(String command) {
            for (var commandPrefix : CommandPrefixes.values()) {
                if (commandPrefix.matches(command)) {
                    return true;
                }
            }
            return false;
        }

        public boolean matches(String command) {
            return command.matches(regex);
        }

        public String getGroup(String command, String groupName) {
            Matcher matcher = Pattern.compile(regex).matcher(command);
            matcher.find();
            return matcher.group(groupName);
        }
    }
}
