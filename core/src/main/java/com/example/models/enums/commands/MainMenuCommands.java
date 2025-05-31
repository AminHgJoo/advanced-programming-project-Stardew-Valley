package com.example.models.enums.commands;

import org.intellij.lang.annotations.Language;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MainMenuCommands implements Command {
    USER_LOGOUT("^user\\s+logout$"),
    SHOW_MENU(Command.SHOW_MENU),
    EXIT_MENU(Command.EXIT_MENU),
    ENTER_MENU(Command.ENTER_MENU);

    private final String regex;

    MainMenuCommands(@Language("Regexp") String regex) {
        this.regex = regex;
    }

    private Matcher getMatcher(String input) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }

    @Override
    public boolean matches(String input) {
        return getMatcher(input).matches();
    }

    @Override
    public String getGroup(String input, String group) {
        Matcher matcher = getMatcher(input);
        matcher.find();
        return matcher.group(group);
    }
}
