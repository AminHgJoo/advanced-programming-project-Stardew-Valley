package com.common.utils;

import dev.morphia.annotations.Embedded;

@Embedded
public class ChatMessage {
    public String message;
    public String sender;
    /**
     * May be <code>null</code> if message is public .
     */
    public String recipient;
    public boolean isPrivate;

    public ChatMessage(String message, String sender, String recipient, boolean isPrivate) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.isPrivate = isPrivate;
    }

    public ChatMessage() {
    }
}
