package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SignInMenuCommands implements Command {
    REGISTER("^register\\s+-u\\s+(?<username>.+?)\\s+-p\\s+(?<password>.+?)\\s+(?<password_confirm>.+?)\\s+-n\\s+(?<nickname>.+?)\\s+-e(?<email>.+?)\\s+-g\\s+(?<gender>.+)$"),
    PICK_QUESTION("^pick\\s+question\\s+-q\\s+(?<question_number>\\d+)\\s+-a\\s+(?<answer>.+?)\\s+-c\\s+(?<answer_confirm>.+)$"),
    LOGIN("^login\\s+-u\\s+(?<username>.+?)\\s+-p\\s+(?<password>.+?)\\s+–stay-logged-in$"),
    FORGET("^forget\\s+password\\s+-u\\s+(?<username>.+)$"),
    ANSWER("^answer\\s+-a\\s+(?<answer>.+)$"),
    SHOW_MENU(Command.SHOW_MENU),
    EXIT_MENU(Command.EXIT_MENU),
    ENTER_MENU(Command.ENTER_MENU);

    private final String regex;

    SignInMenuCommands(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean matches(String input) {
        return getMatcher(input).matches();
    }

    public Matcher getMatcher(String input) {
        return Pattern.compile(regex).matcher(input);
    }

    @Override
    public String getGroup(String input, String group) {
        Matcher matcher = getMatcher(input);
        matcher.find();
        return matcher.group(group);
    }
}
