package com.server.controllers;

import com.common.models.User;
import com.common.models.enums.SecurityQuestion;
import com.server.repositories.UserRepository;
import com.server.utilities.JwtUtil;
import com.server.utilities.Response;
import com.server.utilities.Validation;
import io.javalin.http.Context;

import java.util.HashMap;

public class UserController {
    public void register(Context ctx) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String username = (String) body.get("username");
            String password = (String) body.get("password");
            String passwordConfirm = (String) body.get("passwordConfirm");
            String email = (String) body.get("email");
            String nickname = (String) body.get("nickname");
            String gender = (String) body.get("gender");
            String securityQuestion = (String) body.get("securityQuestion");
            String securityAnswer = (String) body.get("securityAnswer");
            String securityAnswerConfirm = (String) body.get("securityAnswerConfirm");
            if (!Validation.validateUsername(username)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Username is invalid!"));
                return;
            }
            while (UserRepository.findUserByUsername(username) != null) {
                username = username + (int) (Math.random() * 69420);
            }
            if (!password.equals(passwordConfirm)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Passwords do not match!"));
                return;
            }
            if (password.compareToIgnoreCase("random") == 0) {
                password = Validation.createRandomPassword();
            } else {
                if (!Validation.validatePasswordFormat(password)) {
                    ctx.json(Response.BAD_REQUEST.setMessage("Password format is invalid!"));
                    return;
                }
                if (!Validation.validatePasswordSecurity(password).equals("Success")) {
                    ctx.json(Response.BAD_REQUEST.setMessage("Password is not secure!"));
                    return;
                }
            }
            if (!Validation.validateEmail(email)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Email format is invalid!"));
                return;
            }
            if (!securityAnswer.equals(securityAnswerConfirm)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Security Answers don't match!"));
                return;
            }
            User user = new User(gender, email, nickname, Validation.hashPassword(password), username);
            user.setQuestion(SecurityQuestion.getSecurityQuestion(securityQuestion));
            user.setAnswer(securityAnswer);

            UserRepository.saveUser(user);
            String token = JwtUtil.generateToken(user, "user");

            ctx.header("Authorization", token).json(Response.OK.setMessage("User created, password: "
                + Validation.hashPassword(password)).setBody(user));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid request body format"));
            return;
        }
    }

    public void login(Context ctx) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String username = (String) body.get("username");
            String password = (String) body.get("password");

            User user = UserRepository.findUserByUsername(username);
            if (user == null) {
                ctx.json(Response.NOT_FOUND.setMessage("User not found!"));
                return;
            }
            if (!Validation.hashPassword(password).equals(user.getHashedPassword())) {
                ctx.json(Response.BAD_REQUEST.setMessage("Password doesn't match!"));
                return;
            }

            String token = JwtUtil.generateToken(user, "user");
            ctx.header("Authorization", token).json(Response.OK.setMessage("User logged in!").setBody(user));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid request body format"));
            return;
        }
    }

    public void forgetPassword(Context ctx) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String username = (String) body.get("username");
            String password = (String) body.get("newPassword");
            String securityAnswer = (String) body.get("securityAnswer");

            User user = UserRepository.findUserByUsername(username);
            if (user == null) {
                ctx.json(Response.NOT_FOUND.setMessage("User not found!"));
                return;
            }
            if (!user.getAnswer().equals(securityAnswer)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Security Answer doesn't match!"));
                return;
            }
            if (!Validation.validatePasswordFormat(password)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Password format is invalid!"));
                return;
            }
            String passError = Validation.validatePasswordSecurity(password);
            if (!passError.equals("Success")) {
                ctx.json(Response.BAD_REQUEST.setMessage(passError));
                return;
            }

            user.setHashedPassword(Validation.hashPassword(password));
            UserRepository.saveUser(user);

            ctx.json(Response.OK.setMessage("Password has been successfully updated!"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid request body format"));
        }
    }

    public void changePassword(Context ctx) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);

            String newPassword = (String) body.get("newPassword");
            String oldPassword = (String) body.get("oldPassword");
            String id = (String) ctx.attributeMap().get("id");

            User user = UserRepository.findUserById(id);
            if (user == null) {
                ctx.json(Response.NOT_FOUND.setMessage("User not found!"));
                return;
            }
            if (!Validation.validatePasswordFormat(newPassword)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Password format is invalid!"));
                return;
            }
            String passError = Validation.validatePasswordSecurity(newPassword);
            if (!passError.equals("Success")) {
                ctx.json(Response.BAD_REQUEST.setMessage(passError));
                return;
            }
            if (!user.getHashedPassword().equals(Validation.hashPassword(oldPassword))) {
                ctx.json(Response.BAD_REQUEST.setMessage("Password is not correct"));
                return;
            }

            if (oldPassword.equals(newPassword)) {
                ctx.json(Response.OK.setMessage("New password is the same as old password"));
                return;
            }
            user.setHashedPassword(Validation.hashPassword(newPassword));
            UserRepository.saveUser(user);

            ctx.json(Response.OK.setMessage("Password has been successfully updated!"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid request body format"));
        }
    }

    public void changeUsername(Context ctx) {
        try{
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);

            String username = (String) body.get("newUsername");
            String id = (String) ctx.attributeMap().get("id");
            User user = UserRepository.findUserById(id);
            if (user == null) {
                ctx.json(Response.NOT_FOUND.setMessage("User not found!"));
                return;
            }
            if (!Validation.validateUsername(username)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Username is invalid!"));
                return;
            }
            while (UserRepository.findUserByUsername(username) != null) {
                username = username + (int) (Math.random() * 69420);
            }
            user.setUsername(username);
            UserRepository.saveUser(user);
            ctx.json(Response.OK.setMessage("Username has been successfully updated!"));
        }
        catch(Exception e){
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid request body format"));
        }
    }

    public void changeEmail(Context ctx) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String email = (String) body.get("email");
            String id = (String) ctx.attributeMap().get("id");

            User user = UserRepository.findUserById(id);
            if (user == null) {
                ctx.json(Response.NOT_FOUND.setMessage("User not found!"));
                return;
            }
            if (!Validation.validateEmail(email)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Email format is invalid!"));
                return;
            }
            if (user.getEmail().equals(email)) {
                ctx.json(Response.BAD_REQUEST.setMessage("New email is the same as the old email"));
                return;
            }
            user.setEmail(email);
            UserRepository.saveUser(user);

            ctx.json(Response.OK.setMessage("Email has been successfully updated!"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid request body format"));
        }
    }

    public void changeNickname(Context ctx) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String nickname = (String) body.get("nickname");
            String id = (String) ctx.attributeMap().get("id");
            User user = UserRepository.findUserById(id);
            if (user == null) {
                ctx.json(Response.NOT_FOUND.setMessage("User not found!"));
                return;
            }
            if (user.getNickname().equals(nickname)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Nickname is the same as previous"));
                return;
            }
            user.setNickname(nickname);
            UserRepository.saveUser(user);

            ctx.json(Response.BAD_REQUEST.setMessage("Nickname has been successfully updated!"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid request body format"));
        }
    }
}
