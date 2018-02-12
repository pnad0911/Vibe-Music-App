package cse_110.flashback_player;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView sListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        sListView = (ListView) findViewById(R.id.song_list);

        final ArrayList<Song> songList = new ArrayList<Song>();
        songList.add(new Song("Hey, Soul Sister", "Train", "Heyyyyyyy"));
        songList.add(new Song("Hey, Soul Sister1", "Train1", "Heyyyyyyy1"));
        songList.add(new Song("Hey, Soul Sister2", "Train2", "Heyyyyyyy2"));
        songList.add(new Song("Hey, Soul Sister3", "Train3", "Heyyyyyyy3"));
        songList.add(new Song("Hey, Soul Sister4", "Train4", "Heyyyyyyy4"));
        songList.add(new Song("Hey, Soul Sister5", "Train5", "Heyyyyyyy5"));
        songList.add(new Song("Hey, Soul Sister6", "Train6", "Heyyyyyyy6"));
        songList.add(new Song("Hey, Soul Sister7", "Train7", "Heyyyyyyy7"));
        songList.add(new Song("Hey, Soul Sister8", "Train8", "Heyyyyyyy8"));
        songList.add(new Song("Hey, Soul Sister9", "Train9", "Heyyyyyyy9"));

        SongAdapter adapter = new SongAdapter(this, songList);
        sListView.setAdapter(adapter);
    }

}
