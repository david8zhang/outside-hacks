package com.example.dzhang.outsidehacks;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by dzhang on 7/24/16.
 */
public class PendingRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestRecyclerViewAdapter rrViewAdapter;
    private ArrayList<Request> requests = DataManager.requests;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.SOCKET_URL);
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_pending);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView)findViewById(R.id.pending_requests);
        recyclerView.setLayoutManager(llm);

        rrViewAdapter = new RequestRecyclerViewAdapter(requests, this);
        recyclerView.setAdapter(rrViewAdapter);
        handleListenRequests();
    }

    public void handleListenRequests() {
        Emitter.Listener messageListener = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject)args[0];
                        Log.d("REQUESTS", data.toString());
                        try {
                            if(data.getString("status").equals("PENDING")) {
                                handlePendingRequest(data);
                            } else if(data.getString("status").equals("ACCEPTED")) {
                                handleAcceptedRequest(data);
                            }
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        };
        mSocket.on("requests", messageListener);
        mSocket.connect();
    }

    /**
     * Handle a request with type "confirmed"
     * @param data
     */
    public void handleAcceptedRequest(JSONObject data) {
        try {
            Request req = new Request(data.getString("from"), data.getString("target"), data.getString("status"));
            for(int i = 0; i < DataManager.requests.size(); i++) {
                Request pending = DataManager.requests.get(i);
                if(pending.target.equals(req.target) && pending.from.equals(req.from)) {
                    pending.status = "CONFIRMED";
                    rrViewAdapter.notifyItemChanged(i);
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Handle a request with status pending
     * @param data
     */
    public void handlePendingRequest(JSONObject data) {
        try {
            Request req = new Request(data.getString("from"), data.getString("target"), data.getString("status"));
            for(int i = 0; i < DataManager.requests.size(); i++) {
                Request pending = DataManager.requests.get(i);
                if(pending.target.equals(req.target) && pending.from.equals(req.from) && pending.status.equals(req.status)) {
                    DataManager.requests.remove(pending);
                }
            }
            if(!data.getString("from").equals(Build.ID)) {
                DataManager.requests.add(req);
                rrViewAdapter.notifyDataSetChanged();
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

    }
}
