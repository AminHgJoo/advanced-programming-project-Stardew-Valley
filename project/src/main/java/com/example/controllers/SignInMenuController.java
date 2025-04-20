package com.example.controllers;

import com.example.Repositories.UserRepository;
import com.example.models.App;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.User;
import com.example.models.enums.SecurityQuestion;
import com.example.models.enums.types.MenuTypes;
import com.example.utilities.Validation;

public class SignInMenuController extends Controller {

    private static User userOfForgetPassword = null;
    private static String userPassword;
    public static boolean isProgramWaitingForQuestion = false;
    public static boolean isProgramWaitingForAnswer = false;
    private static User userWaitingForQuestion = null;

    public static User getUserOfForgetPassword() {
        return userOfForgetPassword;
    }

    public static void setUserOfForgetPassword(User userOfForgetPassword) {
        SignInMenuController.userOfForgetPassword = userOfForgetPassword;
    }

    public static Response handleAccountRecovery(Request request) {
        User user = userOfForgetPassword;
        String newPass = request.command;
        if (Validation.hashPassword(newPass).equals(user.getHashedPassword())) {
            return new Response(false, "Select a new password!");
        }

        if (newPass.compareToIgnoreCase("random") == 0) {
            newPass = Validation.createRandomPassword();
        } else {
            if (!Validation.validatePasswordFormat(newPass)) {
                return new Response(false, "Password Format is invalid!");
            }
            if (!Validation.validatePasswordSecurity(newPass).equals("Success")) {
                return new Response(false, "Password isn't secure! " +
                        Validation.validatePasswordSecurity(newPass));
            }
        }
        user.setHashedPassword(Validation.hashPassword(newPass));
        UserRepository.saveUser(user);
        userOfForgetPassword = null;
        App.setLoggedInUser(user);
        App.setCurrMenuType(MenuTypes.MainMenu);
        return new Response(true, "Successfully logged in! Password updated to: " + newPass);
    }

    public static Response handleRegister(Request request) {
        String username = request.body.get("username");
        String password = request.body.get("password");
        String email = request.body.get("email");
        String passwordConfirm = request.body.get("passwordConfirm");
        String nickname = request.body.get("nickname");
        String gender = request.body.get("gender");
        if (!Validation.validateUsername(username)) {
            return new Response(false, "Username is invalid!");
        }
        while (UserRepository.findUserByUsername(username) != null) {
            username = username + (int) (Math.random() * 69420);
        }
        if (password.equalsIgnoreCase(passwordConfirm) && password.compareToIgnoreCase("random") == 0) {
            password = Validation.createRandomPassword();
            passwordConfirm = password;
        } else {
            if (!Validation.validatePasswordFormat(password)) {
                return new Response(false, "Password Format is invalid!");
            }
            if (!Validation.validatePasswordSecurity(password).equals("Success")) {
                return new Response(false, "Password isn't secure! " +
                        Validation.validatePasswordSecurity(password));
            }
            if (!password.equals(passwordConfirm)) {
                return new Response(false, "Passwords do not match!");
            }
        }
        if (!Validation.validateEmail(email)) {
            return new Response(false, "Email is invalid!");
        }
        User user = new User(gender, email, nickname, Validation.hashPassword(password), username);
        userWaitingForQuestion = user;
        isProgramWaitingForQuestion = true;
        if (System.getenv("APP_MODE") != null && System.getenv("APP_MODE").equals("TEST")) {
            userPassword = password;
        }
        String message = "User created! Password is: " + password + "\n" +
                "Enter 'pick question -q <question number> -a <answer> -c <confirm answer>>' to choose security question\n" +
                "You can enter 'list questions' command to see possible security questions\n";
        return new Response(true, message);
    }

    public static Response handlePickQuestion(Request request) {
        int questionNumber = Integer.parseInt(request.body.get("questionNumber"));
        String answer = request.body.get("answer");
        String answerConfirm = request.body.get("answerConfirm");
        if (questionNumber < 1 || questionNumber > 4) {
            return new Response(false, "Invalid question number!");
        }
        if (!answer.equals(answerConfirm)) {
            return new Response(false, "Answer doesn't match!");
        }
        User user = getUserWaitingForQuestion();
        user.setAnswer(answer);
        user.setQuestion(SecurityQuestion.values()[questionNumber - 1]);
        user = UserRepository.saveUser(user);
        isProgramWaitingForQuestion = false;
        userWaitingForQuestion = null;
        App.setCurrMenuType(MenuTypes.MainMenu);
        App.setLoggedInUser(user);
        return new Response(true, "Question Picked! Logging in...");
    }

    public static Response handleLogin(Request request) {
        String username = request.body.get("username");
        String password = request.body.get("password");
        String loginFlag = request.body.get("loginFlag");

        User user = UserRepository.findUserByUsername(username);
        if (user == null) {
            return new Response(false, "User not found!");
        }
        if (!Validation.hashPassword(password).equals(user.getHashedPassword())) {
            return new Response(false, "Password doesn't match!");
        }
        if (loginFlag != null) {
            UserRepository.saveStayLoggedInUser(user);
        }
        App.setLoggedInUser(user);
        App.setCurrMenuType(MenuTypes.MainMenu);
        return new Response(true, "Login Successful. Going to Main Menu!");
    }

    public static Response handleForgetPassword(Request request) {
        String username = request.body.get("username");
        User user = UserRepository.findUserByUsername(username);
        if (user == null) {
            return new Response(false, "User not found!");
        }
        userOfForgetPassword = user;
        isProgramWaitingForAnswer = true;
        return new Response(true, "User " + user.getUsername()
                + ": Answer your security question next.");
    }

    public static Response handleAnswer(Request request) {
        if (userOfForgetPassword == null) {
            return new Response(false, "You haven't entered your username.");
        }
        String answer = request.body.get("answer");
        User user = userOfForgetPassword;
        if (!answer.equals(user.getAnswer())) {
            userOfForgetPassword = null;
            isProgramWaitingForAnswer = false;
            return new Response(false, "Answer doesn't match!");
        }
        isProgramWaitingForAnswer = false;
        return new Response(true, "Your answer is correct; select your new password.");
    }

    public static Response handleListQuestions(Request request) {
        Response response = new Response();
        response.setSuccess(true);

        StringBuilder stringBuilder = new StringBuilder("List of questions:\n");
        int index = 1;
        for (SecurityQuestion question : SecurityQuestion.values()) {
            stringBuilder.append(index).append("- ").append(question).append("\n");
            index++;
        }
        response.setMessage(stringBuilder.toString());
        return response;
    }

    public static User getUserWaitingForQuestion() {
        return userWaitingForQuestion;
    }

    public static String getUserPassword() {
        return userPassword;
    }
}
