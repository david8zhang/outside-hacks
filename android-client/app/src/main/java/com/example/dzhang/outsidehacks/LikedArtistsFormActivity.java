package com.example.dzhang.outsidehacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
                getCheckedItems();
            }
        });

        // Dummy data TODO: remove this dummy data later
        List<Artist> artistList = new ArrayList<Artist>();
        artistList.add(new Artist("a", "Vulfpeck"));
        artistList.add(new Artist("b", "Moon Taxi"));
        artistList.add(new Artist("c", "Redlight"));
        artistList.add(new Artist("d", "Ra Ra Riot"));
        artistList.add(new Artist("e", "The Pretty Cool Dudes"));
        artistList.add(new Artist("f", "Vulf Taxi"));
        artistList.add(new Artist("g", "Moon Moon Riot"));
        artistList.add(new Artist("h", "Red Taxi"));
        artistList.add(new Artist("i", "She Ra"));
        artistList.add(new Artist("j", "The Pretty Kool Dudes"));
        artistList.add(new Artist("k", "Riot Peck"));
        artistList.add(new Artist("l", "Steam Greenlight"));
        artistList.add(new Artist("m", "Sun Uber"));
        artistList.add(new Artist("n", "Arr Arr Peaceful Assembly"));
        artistList.add(new Artist("o", "The Pretti Cool Doods"));


        recyclerView = (RecyclerView) findViewById(R.id.checkboxList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ArtistsRecyclerViewAdapter(artistList));
    }

    public void getCheckedItems() {
        List<Artist> likedArtists = new ArrayList<>();
        ArtistsRecyclerViewAdapter adapter = (ArtistsRecyclerViewAdapter) recyclerView.getAdapter();
        for (int i = 0; i < adapter.checkboxes.size(); i ++)
        {
            if (adapter.checkboxes.get(i).isChecked())
            {
                Artist artist = adapter.getValues().get(i);
                likedArtists.add(artist);
                System.out.println(artist);
            }
        }
    }
}
