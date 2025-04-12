package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfileMenuCommands implements Command {
    CHANGE_USERNAME("^change\\s+username\\s+-u\\s+(?<username>.+)$"),
    CHANGE_NICKNAME("^change\\s+nickname\\s+-u\\s+(?<nickname>.+)$"),
    CHANGE_EMAIL("^change\\s+email\\s+-e\\s+(?<email>.+)$"),
    CHANGE_PASSWORD("^change\\s+password\\s+-p\\s+(?<new_password>.+?)\\s+-o\\s+(?<old_password>.+)$"),
    USER_INFO("user\\s+info"),
    SHOW_MENU(Command.SHOW_MENU),
    EXIT_MENU(Command.EXIT_MENU),
    ENTER_MENU(Command.ENTER_MENU);

    private final String regex;

    ProfileMenuCommands(String regex) {
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
