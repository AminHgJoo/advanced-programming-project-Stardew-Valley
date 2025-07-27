package com.common.utils;

import dev.morphia.annotations.Embedded;

@Embedded
public class ChatMessage {
    public String message;
    public String sender;
    /**
     * May be <code>null</code> if message is public .
     */
    public String receiver;
    public boolean isPrivate;

    public ChatMessage(String message, String sender, String receiver, boolean isPrivate) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.isPrivate = isPrivate;
    }

    public ChatMessage() {
    }
}
