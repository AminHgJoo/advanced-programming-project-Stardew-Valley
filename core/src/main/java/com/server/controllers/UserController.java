package com.server.controllers;

import com.common.models.User;
import com.common.models.enums.SecurityQuestion;
import com.common.models.mapModels.Map;
import com.server.repositories.UserRepository;
import com.server.utilities.JwtUtil;
import com.server.utilities.Response;
import com.server.utilities.Validation;
import io.javalin.http.Context;
import org.eclipse.jetty.server.HttpInput;

import java.util.HashMap;

public class UserController {
    public void register(Context ctx) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String username = (String) body.get("username");
            String password = (String) body.get("password");
            String email = (String) body.get("email");
            String nickname = (String) body.get("nickname");
            String gender = (String) body.get("gender");
            String securityQuestion = (String) body.get("securityQuestion");
            String securityAnswer = (String) body.get("securityAnswer");
            if (!Validation.validateUsername(username)) {
                ctx.json(Response.BAD_REQUEST.setMessage("Username is invalid!"));
                return;
            }
            while (UserRepository.findUserByUsername(username) != null) {
                username = username + (int) (Math.random() * 69420);
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
            User user = new User(gender, email, nickname, Validation.hashPassword(password), username);
            user.setQuestion(SecurityQuestion.getSecurityQuestion(securityQuestion));
            user.setAnswer(securityAnswer);

            UserRepository.saveUser(user);
            String token = JwtUtil.generateToken(user, "user");

            ctx.header("Authorization", token).json(Response.OK.setMessage("User created!").setBody(user));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage("Invalid request body format"));
            return;
        }

    }

    public void login(Context ctx) {

    }

    public void forgetPassword(Context ctx) {

    }

    public void changePassword(Context ctx) {

    }

    public void changeUsername(Context ctx) {

    }

    public void changeEmail(Context ctx) {

    }

    public void changeNickname(Context ctx) {

    }
}
