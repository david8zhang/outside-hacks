package com.example.dzhang.outsidehacks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwardzhang on 7/23/16.
 */
public class ArtistsRecyclerViewAdapter extends RecyclerView.Adapter<ArtistsRecyclerViewAdapter.ViewHolder> {

    private final List<Artist> values;
    public List<CheckBox> checkboxes;

    public List<Artist> getValues() {
        return values;
    }

    public ArtistsRecyclerViewAdapter(List<Artist> values) {
        this.values = values;
        checkboxes = new ArrayList<CheckBox>();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_artist_checkbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // TODO: add image of artist
        holder.item = values.get(position);
        holder.checkBox.setText(values.get(position).name);
        checkboxes.add(holder.checkBox);
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
