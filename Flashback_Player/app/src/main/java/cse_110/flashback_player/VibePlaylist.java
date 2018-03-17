package cse_110.flashback_player;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Daniel on 3/4/2018.
 */

public class VibePlaylist implements DatabaseListener, SongDownloadHelper. DownloadCompleteListener {

    /* Entire list of songs */
    private List<Song> entireSongList;
    public static List<Song> upcomingList = new ArrayList<>();
    /* List of viable songs to be placed in the playlist (played b4, not disliked) */
    private HashSet<Song> viableSongs = new HashSet<>();

    /* Priority queue used to build the playlist */
    private PriorityQueue<Song> playlist;

    /* Context provided by NormalActivity */
    private AppCompatActivity activity;

    /* Data for the priority queue */
    OffsetDateTime currentTime;
    Location currentLocation;

    private List<SongListListener> listeners = new ArrayList<>();
    private SongList SongListGen;

    public void reg(SongListListener ls) {
        listeners.add(ls);
    }

    /* Constructor */
    public VibePlaylist(AppCompatActivity activity) {
        this.activity = activity;
        entireSongList = new ArrayList<>();
        Database.loadAllSongs(this);
    }

    public void update(Song song) {

        Log.println(Log.ERROR, "VibePlaylist callback from database", "Song is:" + song.toString());
        Log.println(Log.ERROR, "VibePlaylist callback from database", "Song is playable? " + isPlayable(song));

        Log.e("update", "DatabaseKey is: " + song.getDatabaseKey());

//        entireSongList.clear();
        viableSongs.clear();


        entireSongList.add(song);
        downloadSong(song);

        for (SongListListener l : listeners) {
            l.updateDisplay(getVibeSong());
        }
    }

    public void clearEntireSongList(){
        entireSongList.clear();
    }


    /* Update and return a list of songs in the priority queue based on a location/time */
    public List<Song> getVibeSong() {
        currentTime = OffsetDateTime.now().minusHours(8);
        currentLocation = VibeActivity.getLocation();

        // build priority queue
        Log.e("getVibeSong", "entireSongList size: " + entireSongList.size());
        playlist = new PriorityQueue<>(1, new SongCompare2<>(currentLocation, currentTime));
        // populate viable song set
        for (Song s : entireSongList) {
            if (isPlayable(s)) {
                viableSongs.add(s);
            }
        }

        Log.e("getVibeSong","viableSongs Size: " + Integer.toString(viableSongs.size()));
        // populate playlist based on new data
        for (Song song : viableSongs) {
            if (isPlayable(song)) {
//                downloadSong(song);
                playlist.add(song);
                Log.e("getVibeSong", "DatabaseKey is: " + song.getDatabaseKey());
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
                && song.getLocations() != null
                && song.getScore(currentLocation, currentTime) > 0;
    }

    /* Determines whether a song can be downloaded or not and downloads; false if failed to download
     */
    private boolean downloadSong(Song song) {

        if (song.getDownloadStatus() == null || !song.getDownloadStatus()) {
            SongDownloadHelper downloadHelper = new SongDownloadHelper(song.getSongUrl(), this
                    , activity,song);
            downloadHelper.startDownload();
//            song.setDownloaded();
            return true;
        }
        return false;
    }

    /*
    * Refresh the song list, find the newly downloaded song and assign url to that song
    * Parameter: new url */
    public void downloadCompleted(Song song) {
        song.setDownloaded();
        for (SongListListener ls : listeners) {
            ls.updateDisplay(new ArrayList<Song>(playlist));
            ls.updateDisplay(new HashMap<String, List<Song>>(), new ArrayList<String>());
            upcomingList.add(song);
            ls.updateDisplayUpcoming(upcomingList);
        }
        playlist.clear();viableSongs.clear();entireSongList.clear();
        getVibeSong();
    }
    public void downloadCompleted(String url) { }
}