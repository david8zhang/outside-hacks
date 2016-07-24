package com.example.dzhang.outsidehacks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by dzhang on 7/24/16.
 */
public class ChatViewAdapter <T extends Message> extends RecyclerView.Adapter<ChatViewAdapter.ViewHolder> {

    private ArrayList<T> messages;
    private Context context;

    public ChatViewAdapter(ArrayList<T> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public ChatViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        final ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
