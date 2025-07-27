package com.client.utils;

import com.badlogic.gdx.Input;

import java.util.ArrayList;

public enum Keybinds {
    UP(Input.Keys.W),
    LEFT(Input.Keys.A),
    DOWN(Input.Keys.S),
    RIGHT(Input.Keys.D),
    USE_OR_DROP_ITEM(Input.Keys.C, Input.Buttons.LEFT),
    PERFORM_ACTON(Input.Keys.X, Input.Buttons.RIGHT),
    OPEN_INVENTORY(Input.Keys.E, Input.Keys.ESCAPE),
    OPEN_JOURNAL(Input.Keys.F),
    OPEN_MINIMAP(Input.Keys.M),
    CHANGE_TOOLBAR(Input.Keys.TAB),
    OPEN_CHAT(Input.Keys.T),
    HOTBAR_SLOTS(Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3, Input.Keys.NUM_4, Input.Keys.NUM_5, Input.Keys.NUM_6
        , Input.Keys.NUM_7, Input.Keys.NUM_8, Input.Keys.NUM_9, Input.Keys.NUM_0, Input.Keys.MINUS, Input.Keys.EQUALS),
    SCREENSHOT_MODE(Input.Keys.F4),
    OPEN_CRAFTING(Input.Keys.B),
    OPEN_COOKING(Input.Keys.K),
    OPEN_RADIO(Input.Keys.R),
    SPAWN_CROW(Input.Keys.Z),
    OPEN_FRIDGE(Input.Keys.Q),
    INSPECT_GREENHOUSE(Input.Keys.I);

    public final ArrayList<Integer> keycodes;

    Keybinds(int... args) {
        keycodes = new ArrayList<>();

        for (int keycode : args) {
            keycodes.add(keycode);
        }
    }
}
