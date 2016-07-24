package com.example.dzhang.outsidehacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dzhang.outsidehacks.dummy.DummyContent;

public class FriendList extends AppCompatActivity
        implements FriendFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
    }

    @Override
    public void onListFragmentInteraction(Friend item) {

    }
}
