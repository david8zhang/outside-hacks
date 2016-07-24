package com.example.dzhang.outsidehacks;

/**
 * Created by edwardzhang on 7/23/16.
 */
public class Friend {
    public final String userId;
    public final String username;
    public final String tagline;
    public final int numFriends;
    public final String[] friendIds;
    public final String[] interestedArtists;
    public final int score;

    public Friend(String userId, String username, String tagline, int numFriends,
                  String[] friendIds, String[] interestedArtists, int score) {
        this.userId = userId;
        this.username = username;
        this.tagline = tagline;
        this.numFriends = numFriends;
        this.friendIds = friendIds;
        this.interestedArtists = interestedArtists;
        this.score = score;
    }

    @Override
    public String toString() {
        return username;
    }
}