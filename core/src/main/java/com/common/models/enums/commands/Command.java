package com.common.models.enums.commands;

import org.intellij.lang.annotations.Language;

public interface Command {
    @Language("Regexp")
    String ENTER_MENU = "^menu\\s+enter\\s+(?<menuName>.+)$";
    @Language("Regexp")
    String EXIT_MENU = "^menu\\s+exit$";
    @Language("Regexp")
    String SHOW_MENU = "^show\\s+current\\s+menu$";

    String buildCommand(Object... args);

    default String formatRegex(String regex) {
        String string = regex.replaceAll("\\\\s\\+", " ");
        string = string.replaceAll("\\(\\?<([^>]+)>[^)]+\\)", "%s");
        string = string.replaceAll("\\\\s\\*", " ");
        string = string.replaceAll("\\$", "");
        string = string.replaceAll("\\^", "");

        return string;
    }

    boolean matches(String input);

    String getGroup(String input, String group);
}
