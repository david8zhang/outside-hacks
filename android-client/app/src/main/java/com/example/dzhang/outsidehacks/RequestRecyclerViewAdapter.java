package com.example.dzhang.outsidehacks;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by dzhang on 7/24/16.
 */
public class RequestRecyclerViewAdapter extends RecyclerView.Adapter<RequestRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Request> requests = new ArrayList<>();
    private Context context;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.SOCKET_URL);
        } catch (URISyntaxException e) {}
    }

    public RequestRecyclerViewAdapter(ArrayList<Request> requests, Context context) {
        this.requests = requests;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Button confirm = holder.confirm;
        Button deny = holder.deny;
        Button pending = holder.pending;
        TextView user_id = holder.user_id;
        final Request req = requests.get(position);
        if(req.getFrom().equals(Build.ID)) {
            String self = "Myself";
            user_id.setText(self);
            confirm.setVisibility(View.INVISIBLE);
            deny.setVisibility(View.INVISIBLE);
        } else {
            user_id.setText(req.getFrom());
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean noConfirm = false;
                    if(req.getStatus() != "ACCEPTED") {
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("from", Build.ID);
                            obj.put("target", req.getFrom());
                            obj.put("status", "ACCEPTED");
                            mSocket.emit("requests", obj.toString());
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent toChat = new Intent(context, ChatActivity.class);
                    toChat.putExtra("user_id", req.getFrom());
                    context.startActivity(toChat);
                }
            });
            deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requests.remove(req);
                    RequestRecyclerViewAdapter.this.notifyItemRemoved(position);
                    RequestRecyclerViewAdapter.this.notifyItemRangeChanged(position, requests.size());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView user_id;
        Button confirm;
        Button deny;
        Button pending;

        public ViewHolder(View itemView) {
            super(itemView);

            user_id = (TextView)itemView.findViewById(R.id.user_id);
            confirm = (Button)itemView.findViewById(R.id.confirm);
            deny = (Button)itemView.findViewById(R.id.deny);
        }
    }
}
