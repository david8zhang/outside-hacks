package com.example.dzhang.outsidehacks;

/**
 * Created by dzhang on 7/24/16.
 */
public class Message {
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String from;
    public String target;
    public String message;

    public Message(String from, String target, String message) {
        this.from = from;
        this.target = target;
        this.message = message;
    }
}
