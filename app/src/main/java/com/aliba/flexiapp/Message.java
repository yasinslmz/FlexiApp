package com.aliba.flexiapp;

public class Message {
    private String content;
    private String sender; // "user" or "assistant"

    public Message(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }
}