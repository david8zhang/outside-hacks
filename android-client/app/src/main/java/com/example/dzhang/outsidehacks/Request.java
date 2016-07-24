package com.example.dzhang.outsidehacks;

/**
 * Created by dzhang on 7/24/16.
 */
public class Request {

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

    public String from;
    public String target;

    public Request(String from, String target) {
        this.from = from;
        this.target = target;
    }
}
