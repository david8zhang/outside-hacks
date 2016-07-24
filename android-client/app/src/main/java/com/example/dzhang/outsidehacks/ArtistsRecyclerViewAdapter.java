package com.example.dzhang.outsidehacks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

/**
 * Created by edwardzhang on 7/23/16.
 */
public class ArtistsRecyclerViewAdapter extends RecyclerView.Adapter<ArtistsRecyclerViewAdapter.ViewHolder> {

    private final List<Artist> values;

    public ArtistsRecyclerViewAdapter(List<Artist> values) {
        this.values = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // TODO: add image of artist
        holder.item = values.get(position);
        holder.checkBox.setText(values.get(position).name);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final CheckBox checkBox;
        public Artist item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }
}
