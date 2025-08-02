package com.client.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ConfirmAlert extends Dialog {
    public boolean result = false;

    public ConfirmAlert(String title, String message, Skin skin) {
        super(title, skin);

        setSize(400, 200);

        setPosition(
            (Gdx.graphics.getWidth() - getWidth()) / 2,
            (Gdx.graphics.getHeight() - getHeight()) / 2
        );

        text(message);

        button("Yes", true);  // "true" is the result when this button is clicked
        button("No", false);  // "false" is the result when this button is clicked
    }

}
