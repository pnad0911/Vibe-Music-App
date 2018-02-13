package cse_110.flashback_player;

/**
 * Created by Yutong on 2/10/18.
 */

import android.content.Context;
import android.support.design.widget.TabLayout;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Tab1allsongs extends Fragment {

    private int songIdx;
    private ListView sListView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1allsongs, container, false);

        Bundle bundle1 = this.getArguments();
        final SongPlayer songPlayer = (SongPlayer) bundle1.getParcelable("songPlayer");

        // get items from song list
        SongList songListGen = new SongList();
        final List<Song> songList = songListGen.getAllsong();

        sListView = (ListView) rootView.findViewById(R.id.song_list);
        SongAdapter adapter = new SongAdapter(this.getActivity(), songList);
        sListView.setAdapter(adapter);
        // Handle on click event
        sListView.setClickable(true);
        sListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Song song = (Song) sListView.getItemAtPosition(position);
                songIdx = position;
                int idx = getNextSongIdx(songList);
                Song nextSong = songList.get(idx);

                songPlayer.play(song);
                songPlayer.playNext(nextSong);

            }
        });

        // Actions with song Player
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
                    int idx = getNextSongIdx(songList);
                    songPlayer.playNext(songList.get(idx));
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
                songPlayer.play(songList.get(songIdx));
                int idx = getNextSongIdx(songList);
                songPlayer.playNext(songList.get(idx));
            }

        });

        songPlayer.setEndListener(new SongPlayer.SongPlayerCallback() {
             @Override
             public void callback() {
                 int idx = getNextSongIdx(songList);
                 songIdx = idx+1;
                 songPlayer.play(songList.get(songIdx));
                 idx = getNextSongIdx(songList);
                 songPlayer.playNext(songList.get(idx));
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

    public int getNextSongIdx(List<Song> songs){
        int idx = 0;
        if(songIdx == songs.size()-1){
            idx = 0;
        } else{
            idx=songIdx + 1;
        }
        return idx;
    }
}
