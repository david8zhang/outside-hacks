package com.example.dzhang.outsidehacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * Created by dzhang on 7/23/16.
 */
public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatViewAdapter<Message> chatViewAdapter;
    private ArrayList<Message> messages = new ArrayList<Message>();
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.SOCKET_URL);
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setStackFromEnd(true);
        recyclerView = (RecyclerView)findViewById(R.id.chat_window);
        recyclerView.setLayoutManager(llm);

        Message sample = new Message("1469353441664", "MTC19T", "Hello World!");
        messages.add(sample);
        chatViewAdapter = new ChatViewAdapter<>(messages, this);
        recyclerView.setAdapter(chatViewAdapter);
        handleMessageStream();
    }

    /**
     * Handle the stream of messages from the websockets
     */
    public void handleMessageStream() {
        // Location broadcaster
        Emitter.Listener messageListener = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject)args[0];
                        Log.d("MESSAGES", data.toString());
                        try {
                            Message message = new Message(data.getString("from"), data.getString("target"), data.getString("message"));
                            messages.add(message);
                            chatViewAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        mSocket.on("private-messages", messageListener);
        mSocket.connect();
    }
}
