package cse_110.flashback_player;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView sListView;
    private int songIdx = 0; //index for song list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayList<Song> songList = new ArrayList<Song>();
        songList.add(new Song("Susume Tomorrow", R.raw.after_the_storm, "Sonoda Umi", "Susume Tomorrow"));
        songList.add(new Song("Soldier Game", R.raw.america_religious, "Sonoda Umi, Nishikino Maki, Ayase Eli", "Soldier Game"));

        sListView = (ListView) findViewById(R.id.song_list);
        SongAdapter adapter = new SongAdapter(this, songList);
        sListView.setAdapter(adapter);

        final SongPlayer songPlayer = new SongPlayer(this);

        final Button playButton = (Button) findViewById(R.id.play);
        final Button resetButton = (Button) findViewById(R.id.reset);
        final Button nextButton = (Button) findViewById(R.id.next);
        final Button previousButton = (Button) findViewById(R.id.previous);
        // play and pause are the same botton
        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(songPlayer.isPlaying()) {
                    songPlayer.pause();
                    playButton.setText("Play");
                }
                else if (songPlayer.isPaused()) {
                    songPlayer.resume();
                    playButton.setText("Pause");
                }
                else{
                    songPlayer.play(songList.get(songIdx));
                    Song nextSong;
                    if(songIdx == songList.size()){
                        nextSong = songList.get(0);
                    } else{
                        nextSong = songList.get(songIdx + 1);
                    }
                    songPlayer.playNext(nextSong);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songPlayer.stop();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (songIdx == songList.size()-1){
                    songIdx = 0;
                } else {
                    songIdx += 1;
                }

                songPlayer.play(songList.get(songIdx));
                Song nextSong;
                if(songIdx == songList.size()){
                    nextSong = songList.get(0);
                } else{
                    nextSong = songList.get(songIdx + 1);
                }
                songPlayer.playNext(nextSong);
            }

        });

        // Change song title and artist on song player
        final TextView songTitleView = (TextView) findViewById(R.id.name);
        final TextView songArtistView = (TextView) findViewById(R.id.artist);
        final TextView songAlbumView = (TextView) findViewById(R.id.album);

        songTitleView.setText(songList.get(songIdx).getTitle());
        songArtistView.setText(songList.get(songIdx).getArtist());
        songAlbumView.setText(songList.get(songIdx).getAlbum());



    }

    public void save(String mode) {

        SharedPreferences sharedPreferences = getSharedPreferences("mode", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("currentMode",mode);
        editor.apply();
    }

    public void display(String mode) {
        //EditText en = findViewById(R.id.name);
        SharedPreferences sharedPreferences = getSharedPreferences("mode", MODE_PRIVATE);
        String right = sharedPreferences.getString("currentMode","");
        //String unk = en.getText().toString();
    }

}
