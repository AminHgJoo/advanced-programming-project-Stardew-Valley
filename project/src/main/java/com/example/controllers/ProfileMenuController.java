package com.example.controllers;

import com.example.Repositories.UserRepository;
import com.example.models.App;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.User;
import com.example.utilities.Validation;

public class ProfileMenuController extends Controller {
    public static Response handleChangeUsername(Request request) {
        String username = request.body.get("username");
        User user = App.getLoggedInUser();
        if (!Validation.validateUsername(username)) {
            return new Response(false, "Username is invalid!");
        }
        if (user.getUsername().equals(username)) {
            return new Response(false, "Pick a new username!");
        }
        while (UserRepository.findUserByUsername(username) != null) {
            username = username + (int)(Math.random() * 69420);
        }
        user.setUsername(username);
        UserRepository.saveUser(user);
        return new Response(true, "Username has been changed!");
    }

    public static Response handleUserInfoQuery(Request request) {
        User user = App.getLoggedInUser();
        String username = user.getUsername();
        String nickname = user.getNickname();
        String moneyHighScore = String.valueOf(user.getMoneyHighScore());
        String numberOfGames = String.valueOf(user.getNumberOfGames());
        return new Response(true,
                "Username: " + username + "\n" +
                "nickname: " + nickname + "\n" +
                "moneyHighScore: " + moneyHighScore + "\n" +
                "numberOfGames: " + numberOfGames);

    }

    public static Response handleChangePassword(Request request) {
        String newPassword = request.body.get("newPassword");
        String oldPassword = request.body.get("oldPassword");
        User user = App.getLoggedInUser();
        if (!Validation.validatePasswordFormat(newPassword)) {
            return new Response(false, "New password format is invalid!");
        }
        if (!Validation.validatePasswordSecurity(newPassword).equals("Success")) {
            return new Response(false, "New password isn't secure! "
             + Validation.validatePasswordSecurity(newPassword));
        }
        if (!user.getHashedPassword().equals(Validation.hashPassword(oldPassword))) {
            return new Response(false, "Old password is wrong!");
        }
        if (oldPassword.equals(newPassword)) {
            return new Response(false, "New password is the same as the old password!");
        }
        user.setHashedPassword(Validation.hashPassword(newPassword));
        UserRepository.saveUser(user);
        return new Response(true, "Password changed!");
    }

    public static Response handleChangeEmail(Request request) {
        String email = request.body.get("email");
        User user = App.getLoggedInUser();
        if (!Validation.validateEmail(email)) {
            return new Response(false, "Email is invalid!");
        }
        if (user.getEmail().equals(email)) {
            return new Response(false, "Enter a new email address!");
        }
        user.setEmail(email);
        UserRepository.saveUser(user);
        return new Response(true, "Email has been changed!");
    }

    public static Response handleChangeNickname(Request request) {
        String nickname = request.body.get("nickname");
        User user = App.getLoggedInUser();
        if (user.getNickname().equals(nickname)) {
            return new Response(false, "Choose a new nickname!");
        }
        user.setNickname(nickname);
        UserRepository.saveUser(user);
        return new Response(true, "Nickname has been changed!");
    }

}
