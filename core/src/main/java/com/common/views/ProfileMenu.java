package com.common.views;

import com.common.models.IO.Request;
import com.common.models.IO.Response;
import com.common.models.enums.commands.ProfileMenuCommands;
import com.server.controllers.ProfileMenuController;

public class ProfileMenu implements Menu {

    private static Response getUserInfoResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = ProfileMenuController.handleUserInfoQuery(request);
        return response;
    }

    private static Response getChangeNicknameResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("nickname", ProfileMenuCommands.CHANGE_NICKNAME.getGroup(input, "nickname"));
        response = ProfileMenuController.handleChangeNickname(request);
        return response;
    }

    private static Response getChangeEmailResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("email", ProfileMenuCommands.CHANGE_EMAIL.getGroup(input, "email"));
        response = ProfileMenuController.handleChangeEmail(request);
        return response;
    }

    private static Response getChangePasswordResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("newPassword", ProfileMenuCommands.CHANGE_PASSWORD.getGroup(input, "newPassword"));
        request.body.put("oldPassword", ProfileMenuCommands.CHANGE_PASSWORD.getGroup(input, "oldPassword"));
        response = ProfileMenuController.handleChangePassword(request);
        return response;
    }

    private static Response getChangeUsernameResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("username", ProfileMenuCommands.CHANGE_USERNAME.getGroup(input, "username"));
        response = ProfileMenuController.handleChangeUsername(request);
        return response;
    }

    private static Response getShowMenuResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = ProfileMenuController.handleShowMenu(request);
        return response;
    }

    private static Response getExitMenuResponse(String input) {
        Response response;
        Request request = new Request(input);
        response = ProfileMenuController.handleExitMenu(request);
        return response;
    }

    private static Response getEnterMenuResponse(String input) {
        Response response;
        Request request = new Request(input);
        request.body.put("menuName", ProfileMenuCommands.ENTER_MENU.getGroup(input, "menuName"));
        response = ProfileMenuController.handleEnterMenu(request);
        return response;
    }

    public void handleMenu(String input) {
        Response response = null;

        if (ProfileMenuCommands.ENTER_MENU.matches(input)) {
            response = getEnterMenuResponse(input);
        } else if (ProfileMenuCommands.EXIT_MENU.matches(input)) {
            response = getExitMenuResponse(input);
        } else if (ProfileMenuCommands.SHOW_MENU.matches(input)) {
            response = getShowMenuResponse(input);
        } else if (ProfileMenuCommands.CHANGE_USERNAME.matches(input)) {
            response = getChangeUsernameResponse(input);
        } else if (ProfileMenuCommands.CHANGE_PASSWORD.matches(input)) {
            response = getChangePasswordResponse(input);
        } else if (ProfileMenuCommands.CHANGE_EMAIL.matches(input)) {
            response = getChangeEmailResponse(input);
        } else if (ProfileMenuCommands.CHANGE_NICKNAME.matches(input)) {
            response = getChangeNicknameResponse(input);
        } else if (ProfileMenuCommands.USER_INFO.matches(input)) {
            response = getUserInfoResponse(input);
        } else {
            response = getInvalidCommand();
        }
        printResponse(response);
    }
}
