package com.example.views;

import com.example.controllers.SignInMenuController;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.commands.SignInMenuCommands;

public class SignInMenu implements Menu {

    public void handleMenu(String input) {
        Response response = null;
        if (SignInMenuCommands.LIST_QUESTIONS.matches(input)) {
            response = getListQuestionsResponse(input);
        } else if (SignInMenuController.isProgramWaitingForQuestion) {
            if (SignInMenuCommands.PICK_QUESTION.matches(input)) {
                response = getPickQuestionResponse(input);
            } else {
                response = getInvalidCommand();
            }
        } else if (SignInMenuController.getUserOfForgetPassword() != null) {
            response = getChangePasswordResponse(input);
        } else if (SignInMenuCommands.EXIT_MENU.matches(input)) {
            response = getExitMenuResponse(input);
        } else if (SignInMenuCommands.ENTER_MENU.matches(input)) {
            response = getEnterMenuResponse(input);
        } else if (SignInMenuCommands.SHOW_MENU.matches(input)) {
            response = getShowMenuResponse(input);
        } else if (SignInMenuCommands.ANSWER.matches(input)) {
            response = getAnswerResponse(input);
        } else if (SignInMenuCommands.REGISTER.matches(input)) {
            response = getRegisterResponse(input);
        } else if (SignInMenuCommands.LOGIN.matches(input)) {
            response = getLoginResponse(input);
        } else if (SignInMenuCommands.FORGET.matches(input)) {
            response = getForgetPasswordResponse(input);
        } else {
            response = getInvalidCommand();
        }
        printResponse(response);
    }

    private static Response getChangePasswordResponse(String input) {
        Request request = new Request(input);
        Response response = SignInMenuController.handleAccountRecovery(request);
        return response;
    }

    private static Response getListQuestionsResponse(String input) {
        Request request = new Request(input);
        Response response = SignInMenuController.handleListQuestions(request);
        return response;
    }

    private static Response getExitMenuResponse(String input) {
        Request request = new Request(input);
        Response response = SignInMenuController.handleExitMenu(request);
        return response;
    }

    private static Response getEnterMenuResponse(String input) {
        Request request = new Request(input);
        request.body.put("menuName", SignInMenuCommands.ENTER_MENU.getGroup(input, "menuName"));
        Response response = SignInMenuController.handleEnterMenu(request);
        return response;
    }

    private static Response getShowMenuResponse(String input) {
        Request request = new Request(input);
        Response response = SignInMenuController.handleShowMenu(request);
        return response;
    }

    private static Response getRegisterResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", SignInMenuCommands.REGISTER.getGroup(input, "username"));
        request.body.put("password", SignInMenuCommands.REGISTER.getGroup(input, "password"));
        request.body.put("passwordConfirm", SignInMenuCommands.REGISTER.getGroup(input, "passwordConfirm"));
        request.body.put("nickname", SignInMenuCommands.REGISTER.getGroup(input, "nickname"));
        request.body.put("email", SignInMenuCommands.REGISTER.getGroup(input, "email"));
        request.body.put("gender", SignInMenuCommands.REGISTER.getGroup(input, "gender"));
        Response response = SignInMenuController.handleRegister(request);
        return response;
    }

    private static Response getPickQuestionResponse(String input) {
        Request request = new Request(input);
        request.body.put("questionNumber", SignInMenuCommands.PICK_QUESTION.getGroup(input, "questionNumber"));
        request.body.put("answer", SignInMenuCommands.PICK_QUESTION.getGroup(input, "answer"));
        request.body.put("answerConfirm", SignInMenuCommands.PICK_QUESTION.getGroup(input, "answerConfirm"));
        Response response = SignInMenuController.handlePickQuestion(request);
        return response;
    }

    private static Response getLoginResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", SignInMenuCommands.LOGIN.getGroup(input, "username"));
        request.body.put("password", SignInMenuCommands.LOGIN.getGroup(input, "password"));
        request.body.put("loginFlag", SignInMenuCommands.LOGIN.getGroup(input, "loginFlag"));
        Response response = SignInMenuController.handleLogin(request);
        return response;
    }

    private static Response getForgetPasswordResponse(String input) {
        Request request = new Request(input);
        request.body.put("username", SignInMenuCommands.FORGET.getGroup(input, "username"));
        Response response = SignInMenuController.handleForgetPassword(request);
        return response;
    }

    private static Response getAnswerResponse(String input) {
        Request request = new Request(input);
        request.body.put("answer", SignInMenuCommands.ANSWER.getGroup(input, "answer"));
        Response response = SignInMenuController.handleAnswer(request);
        return response;
    }
}
