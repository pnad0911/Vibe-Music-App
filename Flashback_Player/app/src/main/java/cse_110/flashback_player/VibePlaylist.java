package cse_110.flashback_player;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import static cse_110.flashback_player.Song.databaseRef;

/**
 * Created by Daniel on 3/4/2018.
 */

public class VibePlaylist {

    /* Entire list of songs */
    private List<Song> entireSongList;

    /* List of viable songs to be placed in the playlist (played b4, not disliked) */
    private HashSet<Song> viableSongs;

    /* Priority queue used to build the playlist */
    private PriorityQueue<Song> playlist;
    private PriorityQueue<Song> playlist2;

    /* Context provided by NormalActivity */
    private AppCompatActivity activity;


    /* Data for the priority queue */
    OffsetDateTime currentTime;
    Location currentLocation;

    private List<SongListListener> listeners = new ArrayList<>();
    public void reg(SongListListener ls){
        listeners.add(ls);
    }

    /* Constructor */
    public VibePlaylist(AppCompatActivity activity) {
        this.activity = activity;

        extractFirebase();
        try{
            Thread.sleep(1000);
        }catch(Exception e){
        }

        // initialize set of viable songs
        viableSongs = new HashSet<>();

        // populate viable song set
        for (Song song : entireSongList) {
            // 1. not disliked
            // 2. having valid locations
            // 3. having a valid date
            if (isPlayable(song)) {
                viableSongs.add(song);
            }
        }
    }

    /* Retrieve all songs from firebase */
    private void extractFirebase() {
        DatabaseReference songRef = databaseRef.child("SONGS").getRef();
        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                entireSongList = new ArrayList<Song>();

                for (DataSnapshot dsp : snapshot.getChildren()) {

                    Song song = new Song(dsp.child("title").getValue(String.class),
                            dsp.child("artist").getValue(String.class),
                            dsp.child("album").getValue(String.class),
                            dsp.child("songUrl").getValue(String.class),
                            false);
                    song.setLocations(dsp.child("locations").getValue(t));
                    song.setUserNames(dsp.child("userNames").getValue(n));
                    song.setDate(OffsetDateTime.parse(dsp.child("date").getValue(String.class)));

                    entireSongList.add(song);

                    downloadSong(song);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG1", "Failed to read value.", error.toException());
            }
        });
    }

    /* Update and return a list of songs in the priority queue based on a location/time */
    public List<Song> getVibeSong() {

        OffsetDateTime currentTime = OffsetDateTime.now().minusHours(8);
        if(!LibraryActivity.usingCurrentTime){
            currentTime = LibraryActivity.setTime;
        }

        // build priority queue
        playlist = new PriorityQueue<>(1, new SongCompare2<>(currentLocation, currentTime));

        // populate viable song set
        for (Song s : entireSongList) {
            if (isPlayable(s)) {
                viableSongs.add(s);
            }
        }
   //     playlist2 = new PriorityQueue<>(1, new SongCompare2<>(VibeActivity.getLocation(),currentTime));

        // populate playlist based on new data
        for (Song song : viableSongs) {
            if (isPlayable(song)) {
//                downloadSong(song);
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
        1. Not disliked
        2. Have a current and previous location/date
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
        if (!song.getDownloadStatus()) {
            SongDownloadHelper downloadHelper = new SongDownloadHelper(song.getSongUrl(), LibraryActivity.songListGen, activity);
        //    SongDownloadHelper downloadHelper = new SongDownloadHelper(song.getSongUrl(), VibeActivity.songList, activity);
            downloadHelper.startDownload();
            return true;
        }
        return false;
    }
}
