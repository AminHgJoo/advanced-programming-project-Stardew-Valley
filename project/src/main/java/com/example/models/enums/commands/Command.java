package com.example.models.enums.commands;

public interface Command {
    String ENTER_MENU = "menu\\s+enter\\s+(?<menu_name>.*)";
    String EXIT_MENU = "menu\\s+exit";
    String SHOW_MENU = "show\\s+current\\s+menu";

    boolean matches(String input);

    String getGroup(String input, String group);
}
