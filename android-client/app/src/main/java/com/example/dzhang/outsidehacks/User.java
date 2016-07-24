package com.example.dzhang.outsidehacks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwardzhang on 7/23/16.
 */
public class User {
    public final String userId;
    public final String username;
    public final String tagline;
    public final List<String> friendIds;
    public final List<Artist> likedArtists;

    public User(String userId, String username, String tagline,
                List<String> friendIds, List<Artist> likedArtists) {
        this.userId = userId;
        this.username = username;
        this.tagline = tagline;
        this.friendIds = friendIds;
        this.likedArtists = likedArtists;
    }

    @Override
    public String toString() {
        return username;
    }

    public int getScore(User other) {
        int answer = 100 * getNumCommonLikedArtists(this, other).size();
        answer *= 1 / (double)other.friendIds.size();
        return answer;
    }

    public static List<Artist> getNumCommonLikedArtists(User one, User two) {
        List<Artist> answer = new ArrayList<>();
        for (Artist artist1 : one.likedArtists) {
            for (Artist artist2 : two.likedArtists) {
                if (artist1.artistId.equals(artist2.artistId))
                    answer.add(artist1);
            }
        }
        return answer;
    }
}