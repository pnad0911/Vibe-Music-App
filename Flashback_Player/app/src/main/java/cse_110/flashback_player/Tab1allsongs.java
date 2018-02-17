package cse_110.flashback_player;

/**
 * Created by Yutong on 2/10/18.
 */

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
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
import android.widget.AdapterView.OnItemClickListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tab1allsongs extends Fragment {

    private int songIdx=0;
    private Context context;
    private Song currSong;
    private List<Song> songList;
    private SongPlayer songPlayer;

    public static Map<String,String[]> data;
    public MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1allsongs, container, false);

        /*
        * Get Buttons and TextViews*/
        final Button playButton = (Button) rootView.findViewById(R.id.play);
        final Button resetButton = (Button) rootView.findViewById(R.id.reset);
        final Button nextButton = (Button) rootView.findViewById(R.id.next);
        final Button previousButton = (Button) rootView.findViewById(R.id.previous);

        final TextView songTitleView = (TextView) rootView.findViewById(R.id.name);
        final TextView songArtistView = (TextView) rootView.findViewById(R.id.artist);
        final TextView songAlbumView = (TextView) rootView.findViewById(R.id.album);
        final TextView songTimeView = (TextView) rootView.findViewById(R.id.time);

        /* Get songPlayer from main activity*/
        Bundle bundle1 = this.getArguments();
        songPlayer = (SongPlayer) bundle1.getParcelable("songPlayer");

        // get items from song list
        SongList songListGen = new SongList();
        songList = songListGen.getAllsong();
        currSong = songList.get(songIdx);

        // configure listview
        SongAdapter adapter = new SongAdapter(this.getActivity(), songList);
        final ListView sListView = (ListView) rootView.findViewById(R.id.song_list);
        sListView.setAdapter(adapter);
        // Handle on click event
        sListView.setClickable(true);
        sListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                System.out.println("clicked");
                songIdx = position;
                play();
                changeDisplay(songTitleView, songArtistView, songAlbumView);
            }
        });

        changeDisplay(songTitleView, songArtistView, songAlbumView);
        //songTimeView.setText("-----");

        // play and pause are the same button
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
                    play();
                    playButton.setText("Pause");
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
                songIdx = getNextSongIdx(songList);
                play();
                changeDisplay(songTitleView, songArtistView, songAlbumView);
            }

        });

        previousButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                songIdx = getPreviousSongIdx(songList);
                play();
                changeDisplay(songTitleView, songArtistView, songAlbumView);
            }
        });

        songPlayer.setEndListener(new SongPlayer.SongPlayerCallback() {
             @Override
             public void callback() {
                 songIdx = getNextSongIdx(songList);
                 play();
                 changeDisplay(songTitleView, songArtistView, songAlbumView);
             }
         });

        getData(); // ------------------------- Just Don't Delete This Line :) -----------------------

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

    public int getPreviousSongIdx(List<Song> songs){
        int idx = 0;
        if(songIdx == 0){
            idx = songs.size()-1;
        } else{
            idx=songIdx - 1;
        }
        return idx;
    }

    /* Calls play and nextPlay function in songPlayer*/
    public void play(){
        currSong = songList.get(songIdx);
        songPlayer.play(currSong);
        int idx = getNextSongIdx(songList);
        songPlayer.playNext(songList.get(idx));
//        System.out.println(currSong.getDate());
    }

    /* change display on media player to current playing song*/
    public void changeDisplay(TextView songTitleView, TextView songArtistView, TextView songAlbumView){
        songTitleView.setText(currSong.getTitle());
        songArtistView.setText(currSong.getArtist());
        songAlbumView.setText(currSong.getAlbum());
    }

    // --------------------------------- Here Is The Reason ------------------------------
    public void getData() {
        data = new HashMap<>();
        Field[] raw = cse_110.flashback_player.R.raw.class.getFields();
        for (Field f : raw) {
            try {
                AssetFileDescriptor afd = this.getResources().openRawResourceFd(f.getInt(null));
                mmr.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                String al = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                String ti = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String ar = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                String[] list = new String[3];
                list[0] = ti;list[1] = ar;list[2] = al;
                data.put(f.getName(),list);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
