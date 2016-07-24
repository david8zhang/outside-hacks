package com.example.dzhang.outsidehacks;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class ChatRequestActivity extends AppCompatActivity {

    public TextView usernameView;
    public TextView taglineView;
    public String username;
    public Button sendChatRequestButton;
    public Button sendMeetRequestButton;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.SOCKET_URL);
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_request);

        usernameView = (TextView) findViewById(R.id.username);
        taglineView = (TextView) findViewById(R.id.tagline);
        sendChatRequestButton = (Button) findViewById(R.id.sendChatRequest);
        sendMeetRequestButton = (Button) findViewById(R.id.sendMeetRequest);


        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                username = null;
            } else {
                username = extras.getString("user_id");
            }
        } else {
            username = (String)savedInstanceState.getSerializable("user_id");
        }

        mSocket.connect();


        // TODO: Set these OnClickListeners
        sendChatRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("from", Build.ID);
                    obj.put("target", username);
                    obj.put("status", "PENDING");
                    mSocket.emit("requests", obj.toString());
                    DataManager.requests.add(new Request(Build.ID, username, "PENDING"));
                    onBackPressed();
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        sendMeetRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        String tagline = "I am a cool guy at OutsideLands with my VIP tickets that I won from OutsideHacks";

        usernameView.setText(username);
        taglineView.setText(tagline);
    }
}
