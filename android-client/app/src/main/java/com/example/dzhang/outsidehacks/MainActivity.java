package com.example.dzhang.outsidehacks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                EditText usernameField = (EditText) findViewById(R.id.username_field);
                Intent gotoMap = new Intent(MainActivity.this, MapsActivity.class);
                getIntent().putExtra("Username", usernameField.getText());
                MainActivity.this.startActivity(gotoMap);
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
