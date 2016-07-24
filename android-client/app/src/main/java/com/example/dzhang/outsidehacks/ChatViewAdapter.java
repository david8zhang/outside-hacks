package com.example.dzhang.outsidehacks;

import android.app.ActionBar;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dzhang on 7/24/16.
 */
public class ChatViewAdapter <T extends Message> extends RecyclerView.Adapter<ChatViewAdapter.ViewHolder> {

    private ArrayList<T> messages;
    private Context context;

    public ChatViewAdapter(ArrayList<T> messages, Context context) {
        Log.d("Messages", Integer.toString(messages.size()));
        this.messages = messages;
        this.context = context;
    }

    @Override
    public ChatViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("On Create View Holder", "Create viewholder");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_bubble, parent, false);
        final ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("MESSAGE LENGTH", Integer.toString(messages.size()));
        Message message = messages.get(position);
        String text = message.getMessage();
        TextView my_message = holder.my_message;
        TextView other_message = holder.other_message;
        if(message.getFrom().equals(Build.ID)) {
            Log.d("RENDER CHAT MESSAGE", message.getMessage());
            other_message.setWidth(0);
            other_message.setVisibility(View.INVISIBLE);
            my_message.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            my_message.setText(text);
        } else {
            Log.d("RENDER CHAT MESSAGE", message.getMessage());
            my_message.setWidth(0);
            my_message.setVisibility(View.INVISIBLE);
            other_message.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            other_message.setText(text);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView other_message;
        public TextView my_message;

        public ViewHolder(View itemView) {
            super(itemView);

            other_message = (TextView)itemView.findViewById(R.id.other_message);
            my_message = (TextView)itemView.findViewById(R.id.my_message);

        }
    }
}
