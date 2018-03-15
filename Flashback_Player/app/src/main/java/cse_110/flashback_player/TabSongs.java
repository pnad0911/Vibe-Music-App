package cse_110.flashback_player;

/**
 * Created by Yutong on 2/10/18.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public class TabSongs extends Fragment implements SongListListener {

    private int songIdx=0;
    private Context context;
    private Song currSong;
    private List<Song> songList;
    private SongPlayer songPlayer;
//    public static Map<String,String[]> data;
    public MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    private SongAdapter adapter;

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
        SongList songListGen = new SongList(this.getActivity());
        songListGen.reg(this);
        songList = songListGen.getAllsong();
        adapter = new SongAdapter(this.getActivity(), songList);
        if(!songList.isEmpty()) {
            currSong = songList.get(songIdx);
            changeDisplay(songTitleView, songArtistView, songAlbumView, songTimeView);

            // configure listview
            final ListView sListView = (ListView) rootView.findViewById(R.id.song_list);
            sListView.setAdapter(adapter);
            // Handle on click event
            sListView.setClickable(true);
            sListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    songIdx = position;
                    play();
                    changeDisplay(songTitleView, songArtistView, songAlbumView, songTimeView);
                }
            });
        }

        // play and pause are the same button
        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(songPlayer.isPlaying()) {
                    LibraryActivity.getLocation();
                    songPlayer.pause();
                    playButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                }
                else if (songPlayer.isPaused()) {
                    songPlayer.resume();
                    playButton.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                }
                else{
                    play();
                    playButton.setBackgroundResource(R.drawable.ic_pause_black_24dp);
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
                changeDisplay(songTitleView, songArtistView, songAlbumView, songTimeView);
            }

        });

        previousButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                songIdx = getPreviousSongIdx(songList);
                play();
                changeDisplay(songTitleView, songArtistView, songAlbumView, songTimeView);
            }
        });

        songPlayer.setEndListener(new SongPlayer.SongPlayerCallback() {
             @Override
             public void callback() {
                 songIdx = getNextSongIdx(songList);
                 play();
                 changeDisplay(songTitleView, songArtistView, songAlbumView, songTimeView);
             }
        });

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
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
        Log.println(Log.ERROR, "Tab", "Songurl is: "+currSong.getSongUrl());
        currSong = songList.get(songIdx);
        songPlayer.play(currSong);
        int idx = getNextSongIdx(songList);
        songPlayer.playNext(songList.get(idx));
        Log.println(Log.ERROR, "Tab", "Songurl is: "+currSong.getSongUrl());

        currSong.addLocation(LibraryActivity.getLocation());
        currSong.setDate(OffsetDateTime.now());
        currSong.updateDatabase();
    }

    /* change display on media player to current playing song*/
    public void changeDisplay(TextView songTitleView, TextView songArtistView, TextView songAlbumView, TextView songTimeView){
        Context applicationContext =  LibraryActivity.getContextOfApplication();
        songTitleView.setText(currSong.getTitle());
        songArtistView.setText(currSong.getArtist());
        songAlbumView.setText(currSong.getAlbum());
        if(!isNullDate(currSong, applicationContext)) {

//            OffsetDateTime time = OffsetDateTime.parse(currSong.getDate());
            songTimeView.setText(currSong.getDate() + " at Coordinates ( " +
                    currSong.previousLocation().first+ ", " +
                    currSong.previousLocation().second + " )");
        }
        else {
            songTimeView.setText("N/A");
        }

        //TODO currSong.addUser(userid);
    }

    private boolean isNullDate(Song song, Context context) {
        if(song.getDate() == null || song.getLocations() == null) return true;
        else return false;
    }
    public void updateDisplay(List<Song> list) {
        songList.clear();
        songList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void updateDisplay(Map<String, List<Song>> map, List<String> albumNames) { }
}
