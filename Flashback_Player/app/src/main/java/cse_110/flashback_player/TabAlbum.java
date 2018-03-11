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
import java.util.List;
import java.util.Map;

public class TabAlbum extends Fragment {

    private int songIdx = 0;
    private ExpandableListView sListView;
    private List<Song> songList;
    private SongPlayer songPlayer;
    private Song currSong;

    public static Map<String,String[]> data;
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
        final SongList songListGen = new SongList(this.getActivity());
        List<String> albumNames = songListGen.getListOfAlbum();

        sListView = rootView.findViewById(R.id.album_list);
        final AlbumAdapterExpandable adapter = new AlbumAdapterExpandable(this.getActivity(), albumNames,songListGen.getB());
        sListView.setAdapter(adapter);

        // Handle on click event
        sListView.setClickable(true);
//        sListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                String aName = (String) sListView.getItemAtPosition(position);
//                songList = songListGen.getListOfSong(aName);
//                songIdx = 0;
//                play(songTitleView, songArtistView, songAlbumView,songTimeView);
//            }
//        });
        sListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String aName = (String) adapter.getGroup(groupPosition);
                songIdx = childPosition;
                songList = songListGen.getListOfSong(aName);
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
                if(songPlayer.isPlaying()) {
                    songPlayer.pause();
                    playButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                }
                else if (songPlayer.isPaused()) {
                    songPlayer.resume();
                    playButton.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                }
                else{
                    play(songTitleView, songArtistView, songAlbumView,songTimeView);
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
                songIdx = getNextSongIdx();
                play(songTitleView, songArtistView, songAlbumView,songTimeView);
            }

        });

        previousButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                songIdx = getPreviousSongIdx();
                play(songTitleView, songArtistView, songAlbumView ,songTimeView);
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
        if(songIdx == 0){
            idx = songList.size()-1;
        } else{
            idx=songIdx - 1;
        }
        return idx;
    }

    /* change display on media player to current playing song*/
    public void changeDisplay(TextView songTitleView, TextView songArtistView, TextView songAlbumView,TextView songTimeView){
        Context applicationContext =  NormalActivity.getContextOfApplication();
        songTitleView.setText(currSong.getTitle());
        songArtistView.setText(currSong.getArtist());
        songAlbumView.setText(currSong.getAlbum());
        if(!isNullDate(currSong,applicationContext)) {
            OffsetDateTime time = OffsetDateTime.parse(currSong.getDate());
//            songTimeView.setText(time.getDayOfWeek().toString() + "  " + time.getHour() + " O'clock  at Coordinates ( " +
//                    currSong.getLocations().get(0).first+
//                    ":"+currSong.getLocations().get(0).second + " )");
        }
        else {
            songTimeView.setText("N/A");
        }

//        System.out.println("Yolo --------------------" + NormalActivity.getLocation().getLatitude());
        currSong.addLocation(NormalActivity.getLocation());
        currSong.setDate(OffsetDateTime.now());

    }


    public boolean isNullDate(Song song,Context context) {
        if(song.getDate() == null) return true;
        else return false;
    }

}