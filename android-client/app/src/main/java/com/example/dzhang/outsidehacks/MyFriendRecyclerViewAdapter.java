package com.example.dzhang.outsidehacks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dzhang.outsidehacks.dummy.DummyContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Friend}
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFriendRecyclerViewAdapter extends RecyclerView.Adapter<MyFriendRecyclerViewAdapter.ViewHolder> {

    private final List<Friend> mValues;
    private final FriendFragment.OnListFragmentInteractionListener mListener;

    public MyFriendRecyclerViewAdapter(List<Friend> items,
                                       FriendFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.usernameView.setText(mValues.get(position).username);
        holder.taglineView.setText(mValues.get(position).tagline);
        holder.distanceView.setText("22m");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView usernameView;
        public final TextView taglineView;
        public final TextView distanceView;
        public Friend mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            usernameView = (TextView) view.findViewById(R.id.username);
            taglineView = (TextView) view.findViewById(R.id.tagline);
            distanceView = (TextView) view.findViewById(R.id.distance);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + usernameView.getText() + "'";
        }
    }
}
