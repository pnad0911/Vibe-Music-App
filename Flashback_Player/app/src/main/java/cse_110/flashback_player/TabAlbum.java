package cse_110.flashback_player;

/**
 * Created by Yutong on 2/10/18.
 */

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabAlbum extends Fragment implements SongListListener{

    private int songIdx = 0;
    private ExpandableListView sListView;
    private List<Song> songList = new ArrayList<>();
    private SongPlayer songPlayer;
    private Song currSong;
    private AlbumAdapterExpandable adapter;
    private Map<String, List<Song>> map;
    public List<String> albumNames;
    public MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2album, container, false);

        // Actions with song Player
        Bundle bundle1 = getArguments();
        songPlayer = (SongPlayer) bundle1.getParcelable("songPlayer");

        final Button playButton = (Button) rootView.findViewById(R.id.play);
        final Button resetButton = (Button) rootView.findViewById(R.id.reset);
        final Button nextButton = (Button) rootView.findViewById(R.id.next);
        final Button previousButton = (Button) rootView.findViewById(R.id.previous);

        // Change song title and artist on song player
        final TextView songTitleView = (TextView) rootView.findViewById(R.id.name);
        final TextView songArtistView = (TextView) rootView.findViewById(R.id.artist);
        final TextView songAlbumView = (TextView) rootView.findViewById(R.id.album);
        final TextView songTimeView = (TextView) rootView.findViewById(R.id.timeAl);

        // get items from song list
        albumNames = LibraryActivity.songListGen.getListOfAlbum();
        map = LibraryActivity.songListGen.getMap();
        sListView = rootView.findViewById(R.id.album_list);
        adapter = new AlbumAdapterExpandable(this.getActivity(), albumNames, map);
        sListView.setAdapter(adapter);

        // Handle on click event
        sListView.setClickable(true);

        sListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String aName = (String) adapter.getGroup(groupPosition);
                songIdx = childPosition;
                songList = LibraryActivity.songListGen.getListOfSong(aName);
                play(songTitleView, songArtistView, songAlbumView, songTimeView);
                return false;
            }
        });

        songPlayer.setEndListener(new SongPlayer.SongPlayerCallback() {
            @Override
            public void callback() {
                songIdx = getNextSongIdx();
                play(songTitleView, songArtistView, songAlbumView,songTimeView);
            }
        });


        // play and pause are the same button
        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (!songList.isEmpty()) {
                    if (songPlayer.isPlaying()) {
                        songPlayer.pause();
                        playButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                    } else if (songPlayer.isPaused()) {
                        songPlayer.resume();
                        playButton.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    } else {
                        play(songTitleView, songArtistView, songAlbumView, songTimeView);
                        playButton.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    }
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
                if (!songList.isEmpty()) {
                    songIdx = getNextSongIdx();
                    play(songTitleView, songArtistView, songAlbumView, songTimeView);
                }
            }

        });


        previousButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!songList.isEmpty()) {
                    songIdx = getPreviousSongIdx();
                    play(songTitleView, songArtistView, songAlbumView, songTimeView);
                }
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
    /* Calls play and nextPlay function in songPlayer*/
    public void play(TextView songTitleView, TextView songArtistView, TextView songAlbumView ,TextView songTimeView){
        currSong = songList.get(songIdx);
        changeDisplay(songTitleView, songArtistView, songAlbumView, songTimeView);
        songPlayer.play(currSong);
        int idx = getNextSongIdx();
        songPlayer.playNext(songList.get(idx));
    }

    public int getNextSongIdx(){
        int idx = 0;
        if(songIdx == songList.size()-1){
            idx = 0;
        } else{
            idx=songIdx + 1;
        }
        return idx;
    }


    public int getPreviousSongIdx(){
        int idx = 0;
        if (songIdx == 0) {
            idx = songList.size() - 1;
        } else {
            idx = songIdx - 1;
        }
        return idx;
    }

    /* change display on media player to current playing song*/
    public void changeDisplay(TextView songTitleView, TextView songArtistView, TextView songAlbumView,TextView songTimeView){
        Context applicationContext =  LibraryActivity.getContextOfApplication();
        songTitleView.setText(currSong.getTitle());
        songArtistView.setText(currSong.getArtist());
        songAlbumView.setText(currSong.getAlbum());
        if(!isNullDate(currSong,applicationContext)) {
            OffsetDateTime time = OffsetDateTime.parse(currSong.getDate());
            songTimeView.setText(currSong.getDate()+ " AT ( " +
                    currSong.allLocations().get(0).first+
                    ":"+currSong.allLocations().get(0).second + " )");
        }
        else {
            songTimeView.setText("N/A");
        }
        currSong.addLocation(LibraryActivity.getLocation());
        currSong.setDate(OffsetDateTime.now());
        Database.updateDatabase(currSong);
    }
    public boolean isNullDate(Song song,Context context) {
        if(song.getDate() == null) return true;
        else return false;
    }
    public void updateDisplay(Map<String, List<Song>> map, List<String> albumNames) {
        this.map.clear(); this.albumNames.clear();
        this.map.putAll(map); this.albumNames.addAll(albumNames);
        adapter.notifyDataSetChanged();
    }

    public void updateDisplay(List<Song> list) { }
    public void updateDisplayUpcoming(List<Song> list) { }
}
