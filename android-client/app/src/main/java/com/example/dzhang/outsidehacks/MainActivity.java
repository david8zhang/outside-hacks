package com.example.dzhang.outsidehacks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button to navigate to map activity
        Button button = (Button)findViewById(R.id.gotomap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoMap = new Intent(MainActivity.this, MapsActivity.class);
                MainActivity.this.startActivity(gotoMap);
            }
        });

        Button goToArtists = (Button) findViewById(R.id.goToArtistCheckboxes);
        Button goToFriendList = (Button) findViewById(R.id.goToFriendList);

        goToArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToArtists();
            }
        });

        goToFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToFriendList();
            }
        });

        Button goToChatList = (Button)findViewById(R.id.goToChat);
        goToChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToChat = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(goToChat);
            }
        });
    }

    private void GoToFriendList() {
        Intent intent = new Intent(this, FriendList.class);
        startActivity(intent);
    }

    private void GoToArtists() {
        Intent intent = new Intent(this, LikedArtistsFormActivity.class);
        startActivity(intent);
    }
}
