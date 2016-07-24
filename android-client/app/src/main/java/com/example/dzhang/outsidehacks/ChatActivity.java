package com.example.dzhang.outsidehacks;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private EditText messageCompose;
    private String username;
    private ChatRecyclerViewAdapter<Message> chatViewAdapter;
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

        Button like = (Button)findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String message = "LIKED! You've Sent a Like!";
                    JSONObject obj = new JSONObject();
                    obj.put("from", Build.ID);
                    obj.put("target", username);
                    obj.put("message", message);
                    obj.put("status", "LIKE");
                    mSocket.emit("message", obj.toString());
                    Message render_msg = new Message(Build.ID, username, message, "LIKE");
                    messages.add(render_msg);
                    chatViewAdapter.notifyDataSetChanged();
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        messageCompose = (EditText)findViewById(R.id.enter_message);
        Button submitMsg = (Button)findViewById(R.id.submit_message);
        submitMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageCompose.getText().toString();
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("from", Build.ID);
                    obj.put("target", username);
                    obj.put("message", message);
                    mSocket.emit("message", obj.toString());
                    messageCompose.setText("");
                    Message render_msg = new Message(Build.ID, username, message);
                    messages.add(render_msg);
                    chatViewAdapter.notifyDataSetChanged();
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            // Call smooth scroll
                            recyclerView.smoothScrollToPosition(chatViewAdapter.getItemCount());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setStackFromEnd(true);
        recyclerView = (RecyclerView)findViewById(R.id.chat_window);
        recyclerView.setLayoutManager(llm);

        chatViewAdapter = new ChatRecyclerViewAdapter<>(messages, this);
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
                            if(!data.has("status")) {
                                messages.add(message);
                                chatViewAdapter.notifyDataSetChanged();
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Call smooth scroll
                                        recyclerView.smoothScrollToPosition(chatViewAdapter.getItemCount());
                                    }
                                });
                            }
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
