package com.example.models.enums.commands;

import org.intellij.lang.annotations.Language;

public interface Command {
    @Language("Regexp")
    String ENTER_MENU = "^menu\\s+enter\\s+(?<menuName>.+)$";
    @Language("Regexp")
    String EXIT_MENU = "^menu\\s+exit$";
    @Language("Regexp")
    String SHOW_MENU = "^show\\s+current\\s+menu$";

    boolean matches(String input);

    String getGroup(String input, String group);
}
