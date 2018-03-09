package cse_110.flashback_player;

import java.util.ArrayList;

/**
 * Created by Yutong on 3/8/18.
 */

public class SongListRefresher implements SongObserver {

    SongList songList;
//    ArrayList<Song> songs = new ArrayList<>();

    SongListRefresher(Song song, SongList sl){
        songList = sl;
        song.reg(this);
    }

    // add songs to be monitored
    public void addSong(Song song){
        song.reg(this);
    }

    public void update(){
        songList.refresh();
    }
}
