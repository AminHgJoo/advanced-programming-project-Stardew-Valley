package com.example.views;

import com.example.Repositories.UserRepository;
import com.example.models.App;
import com.example.models.User;
import com.example.models.enums.types.MenuTypes;
import com.example.utilities.Connection;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Scanner;

public class AppView {
    public final static Scanner scanner = new Scanner(System.in);

    static {
        Dotenv.configure()
                .directory(System.getProperty("user.dir") + "/project/src/main/java/com/example/configs")
                .filename("env." + System.getenv("APP_MODE").toLowerCase())
                .systemProperties()
                .load();
        Connection.getDatabase();
        User user = UserRepository.getStayLoggedInUser();
        App.setLoggedInUser(user);
        if (App.getLoggedInUser() != null) {
            App.setCurrMenuType(MenuTypes.MainMenu);
        }
    }

    public void run() {
        while (App.getCurrMenuType() != MenuTypes.ExitMenu) {
            String input = scanner.nextLine().trim();
            App.getCurrMenuType().getMenu().handleMenu(input);
        }
    }
}
