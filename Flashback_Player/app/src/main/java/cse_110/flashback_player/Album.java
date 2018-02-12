package cse_110.flashback_player;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Daniel on 2/9/2018.
 */

public class Album {

    private String title;
    private LinkedList<Song> songs;
    private static LinkedList<Album> albums;

    public Album(String title, List<Song> songs){
        setTitle(title);
        setSongs(songs);
    }

    public Album(Album album){
        setTitle(new String(album.getTitle()));
        setSongs(new LinkedList<>(album.getSongs()));
    }

    private void setTitle(String title){
        this.title = title  ;
    }

    private void setSongs(List<Song> songs){
        ListIterator<Song> songIterator = songs.listIterator();

        while (songIterator.hasNext()) {
            Song nextSong = songIterator.next();
            if (nextSong.getAlbum().equals(title)) {
                songs.add(nextSong);
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public List<Song> getSongs(){
        return songs;
    }

}
