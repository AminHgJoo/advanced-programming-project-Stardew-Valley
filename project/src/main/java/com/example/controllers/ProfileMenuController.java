package com.example.controllers;

import com.example.Repositories.UserRepository;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.User;
import com.example.utilities.Validation;

public class ProfileMenuController extends Controller {
    public static Response handleChangeUsername(Request request) {
        String username = request.body.put("username", request.body.get("username"));
        User user = UserRepository.getCurrentUser();
        if (!Validation.validateUsername(username)) {
            return new Response(false, "Username is invalid!");
        }
        if (user.getUsername().equals(username)) {
            return new Response(false, "Username is already taken!");
        }
        while (UserRepository.findUserByUsername(username) != null) {
            username = username + Math.random() % 42069;
        }
        user.setUsername(username);
        UserRepository.saveUser(user);
        return new Response(true, "Username has been changed!");
    }

    public static Response handleUserInfoQuery(Request request) {
        User user = UserRepository.getCurrentUser();
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
        User user = UserRepository.getCurrentUser();
        if (!Validation.validatePassword(newPassword)) {
            return new Response(false, "New password is invalid!");
        }
        if (!user.getPassword().equals(oldPassword)) {
            return new Response(false, "old password is invalid!");
        }
        if (user.getPassword().equals(newPassword)) {
            return new Response(false, "New password is the same!");
        }
        user.setPassword(newPassword);
        UserRepository.saveUser(user);
        return new Response(true, "Password changed!");
    }

    public static Response handleChangeEmail(Request request) {
        String email = request.body.get("email");
        User user = UserRepository.getCurrentUser();
        if (!Validation.validateEmail(email)) {
            return new Response(false, "Email is invalid!");
        }
        if (user.getEmail().equals(email)) {
            return new Response(false, "Email is already taken!");
        }
        user.setEmail(email);
        UserRepository.saveUser(user);
        return new Response(true, "Email has been changed!");
    }

    public static Response handleChangeNickname(Request request) {
        String nickname = request.body.get("nickname");
        User user = UserRepository.getCurrentUser();
        if (user.getNickname().equals(nickname)) {
            return new Response(false, "Nickname is already taken!");
        }
        user.setNickname(nickname);
        UserRepository.saveUser(user);
        return new Response(true, "Nickname has been changed!");
    }

}
