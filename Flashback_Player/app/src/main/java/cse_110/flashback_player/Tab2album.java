package cse_110.flashback_player;

/**
 * Created by Yutong on 2/10/18.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Tab2album extends Fragment { //TODO: to be changed to album list and album functionalities

    private int songIdx;
    private ListView sListView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2album, container, false);

        // get items from song list
        final ArrayList<Song> songList = new ArrayList<Song>();
        songList.add(new Song("Susume Tomorrow", R.raw.susume_tomorrow, "Sonoda Umi", "Susume Tomorrow"));
        songList.add(new Song("Soldier Game", R.raw.soldier_game, "Sonoda Umi, Nishikino Maki, Ayase Eli", "Soldier Game"));

        sListView = (ListView) rootView.findViewById(R.id.song_list);
        SongAdapter adapter = new SongAdapter(this.getActivity(), songList);
        sListView.setAdapter(adapter);

        // Actions with song Player
        Bundle bundle1 = getArguments();
        final SongPlayer songPlayer = (SongPlayer) bundle1.getParcelable("songPlayer");

        final Button playButton = (Button) rootView.findViewById(R.id.play);
        final Button resetButton = (Button) rootView.findViewById(R.id.reset);
        final Button nextButton = (Button) rootView.findViewById(R.id.next);
        final Button previousButton = (Button) rootView.findViewById(R.id.previous);

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
                    if(songIdx == songList.size()-1){
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
                System.out.println(songList.get(songIdx).getTitle());
                Song nextSong;
                if(songIdx == songList.size()-1){
                    nextSong = songList.get(0);
                } else{
                    nextSong = songList.get(songIdx + 1);
                }
                songPlayer.playNext(nextSong);
            }

        });

        // Change song title and artist on song player
        final TextView songTitleView = (TextView) rootView.findViewById(R.id.name);
        final TextView songArtistView = (TextView) rootView.findViewById(R.id.artist);
        final TextView songAlbumView = (TextView) rootView.findViewById(R.id.album);

        songTitleView.setText(songList.get(songIdx).getTitle());
        songArtistView.setText(songList.get(songIdx).getArtist());
        songAlbumView.setText(songList.get(songIdx).getAlbum());

        return rootView;
    }


}
