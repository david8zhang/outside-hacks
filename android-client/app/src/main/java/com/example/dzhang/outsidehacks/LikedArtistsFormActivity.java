package com.example.dzhang.outsidehacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class LikedArtistsFormActivity extends AppCompatActivity {

    public Button submit;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_artists_form);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: implement this
            }
        });

        // Dummy data TODO: remove this dummy data later
        List<Artist> artistList = new ArrayList<Artist>();
        artistList.add(new Artist("a", "Vulfpeck"));
        artistList.add(new Artist("b", "Moon Taxi"));
        artistList.add(new Artist("c", "Redlight"));
        artistList.add(new Artist("d", "Ra Ra Riot"));
        artistList.add(new Artist("e", "The Pretty Cool Dudes"));

        recyclerView = (RecyclerView) findViewById(R.id.checkboxList);
        recyclerView.setAdapter(new ArtistsRecyclerViewAdapter(artistList));

    }
}
