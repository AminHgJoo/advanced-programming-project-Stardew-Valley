package com.common.views;

import com.common.models.IO.Response;

public interface Menu {
    void handleMenu(String input);

    default void printResponse(Response response) {
        System.out.println(response.getMessage());
    }

    default Response getInvalidCommand() {
        return new Response(false, "Invalid command");
    }

}
