package com.example.dzhang.outsidehacks;

/**
 * Created by dzhang on 7/23/16.
 */
public class User {
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNum_friends() {
        return num_friends;
    }

    public void setNum_friends(int num_friends) {
        this.num_friends = num_friends;
    }

    public String[] getFriends() {
        return friends;
    }

    public void setFriends(String[] friends) {
        this.friends = friends;
    }

    public String[] getInterested_artists() {
        return interested_artists;
    }

    public void setInterested_artists(String[] interested_artists) {
        this.interested_artists = interested_artists;
    }

    private String user_id;
    private String username;
    private int score;
    private int num_friends;
    private String[] friends;
    private String[] interested_artists;


}
