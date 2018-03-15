package cse_110.flashback_player;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Daniel on 3/4/2018.
 */

public class VibePlaylist implements DatabaseListener{

    /* Entire list of songs */
    private List<Song> entireSongList;

    /* List of viable songs to be placed in the playlist (played b4, not disliked) */
    private HashSet<Song> viableSongs = new HashSet<>();

    /* Priority queue used to build the playlist */
    private PriorityQueue<Song> playlist;

    /* Context provided by NormalActivity */
    private AppCompatActivity activity;

    private GenericTypeIndicator<ArrayList<HashMap<String,String>>> t = new GenericTypeIndicator<ArrayList<HashMap<String,String>>>() {};
    private GenericTypeIndicator<ArrayList<String>> n = new GenericTypeIndicator<ArrayList<String>>() {};

    /* Constructor */
    public VibePlaylist(AppCompatActivity activity) {
        this.activity = activity;
        entireSongList = new ArrayList<>();
        Database.loadAllSongs(this);
    }

    public void update(Song song){

        Log.println(Log.ERROR, "VibePlaylist callback from database", "Song is:" + song.toString());

        entireSongList.add(song);
        downloadSong(song);

        // populate viable song set
        for (Song s : entireSongList) {
            if (isPlayable(s)) {
                viableSongs.add(s);
            }
        }
    }

    /* Update and return a list of songs in the priority queue based on a location/time */
    public List<Song> getVibeSong() {
        OffsetDateTime currentTime = OffsetDateTime.now().minusHours(8);

        // build priority queue
        playlist = new PriorityQueue<>(1, new SongCompare<>(VibeActivity.getLocation(), currentTime));

        // populate playlist based on new data
        for (Song song : viableSongs) {
            if (isPlayable(song)) {
                downloadSong(song);
                playlist.add(song);
            }
        }

        // duplicate playlist to return in a list
        PriorityQueue<Song> returnPQ = new PriorityQueue<>(playlist);
        ArrayList<Song> returnList = new ArrayList<>();

        // pop off elements from PQ into list
        while (!returnPQ.isEmpty()) {
            returnList.add(returnPQ.poll());
        }

        return returnList;
    }

    /* Updates the status of a song if it is favorited */
    public void likeSong(Song song) {
        song.like();

        viableSongs.add(song);

        // reinsert song to update its priority
        if (isPlayable(song)) {
            playlist.remove(song);
            playlist.add(song);
        }
    }

    /* Updates the status of a song if it is disliked */
    public void dislikeSong(Song song) {
        song.dislike();
        viableSongs.remove(song);
        playlist.remove(song);
    }

    /* Updates the status of a song if it is neutral */
    public void neutralSong(Song song) {
        song.neutral();
        viableSongs.add(song);
        if (isPlayable(song) && !playlist.contains(song)) {
            playlist.add(song);
        }
    }

    /* Adds a song to the viable song list */
    public void addSong(Song song) {
        viableSongs.add(song);
        if (isPlayable(song) && !playlist.contains(song)) {
            playlist.add(song);
        }
    }

    /* 1 -> favorited, 0 -> neutral, -1 -> disliked */
    public int getSongStatus(Song song) {
        return song.getSongStatus();
    }

    /* Determines whether a song is viable for playability; song must be:
     *  1. not disliked
     *  2. having valid locations
     *  3. having a valid date
     */
    private boolean isPlayable(Song song) {
        return song.getSongStatus() != -1
                && song.getDate() != null
                && song.getLocations() != null;
    }

    /* Determines whether a song can be downloaded or not and downloads; false if failed to download
     */
    private boolean downloadSong(Song song) {

        if (!song.getDownloadStatus()) {
            SongDownloadHelper downloadHelper = new SongDownloadHelper(song.getSongUrl(), VibeActivity.songList, activity);
            downloadHelper.startDownload();
            return true;
        }
        return false;
    }
}
