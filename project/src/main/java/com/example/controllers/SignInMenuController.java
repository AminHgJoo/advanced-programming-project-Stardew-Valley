package com.example.controllers;

import com.example.Repositories.UserRepository;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.User;
import com.example.utilities.Validation;

public class SignInMenuController extends Controller {
    public static Response handleRegister(Request request) {
        String username = request.body.get("username");
        String password = request.body.get("password");
        String email = request.body.get("email");
        String passwordConfirm = request.body.get("passwordConfirm");
        String nickname = request.body.get("nickname");
        String gender = request.body.get("gender");
        if(!Validation.validateUsername(username)) {
            return new Response(false , "Username is invalid!");
        }
        while(UserRepository.findUserByUsername(username) != null){
            username = username + Math.random()%42069;
        }
        if(!Validation.validateEmail(email)) {
            return new Response(false , "Email is invalid!");
        }
        if(!Validation.validatePassword(password)) {
            return new Response(false , "Password is invalid!");
        }
        if(!password.equals(passwordConfirm)) {
            return new Response(false , "Passwords do not match!");
        }
        // TODO rand pass
        // TODO question
        // TODO bonus
        User user = new User(gender,email,nickname,password,username);
        UserRepository.saveUser(user);
        return new Response(false , "User created!");
    }

    public static Response handlePickQuestion(Request request) {
    }

    public static Response handleLogin(Request request) {
    }

    public static Response handleForgetPassword(Request request) {
    }

    public static Response handleAnswer(Request request) {
    }
}
