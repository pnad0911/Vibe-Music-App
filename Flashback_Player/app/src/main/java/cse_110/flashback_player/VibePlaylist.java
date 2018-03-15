package cse_110.flashback_player;

import android.content.Context;
import android.util.Log;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    /* Songs in the priority queue stored as a list for sorting */
    private List<Song> playlistSorted;

    /* Context provided by NormalActivity */
    private Context context;

    private DatabaseReference databaseReference;

    private FirebaseDatabase firebaseDatabase;

    /* Constructor */
    public VibePlaylist(DatabaseReference databaseReference, FirebaseDatabase firebaseDatabase) {
        this.databaseReference = databaseReference;
        this.firebaseDatabase = firebaseDatabase;

        extractFirebase();

        // initialize set of viable songs
        viableSongs = new HashSet<>();

        // initialize context
        context = LibraryActivity.getContextOfApplication();

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
        songRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                entireSongList = new ArrayList<Song>();

                for (DataSnapshot dsp : snapshot.getChildren()) {
                    entireSongList.add(dsp.getValue(Song.class));
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

        // build priority queue
        playlist = new PriorityQueue<>(1, new SongCompare<>(VibeActivity.getLocation(), currentTime));

        // populate playlist based on new data
        for (Song song : viableSongs) {
            if (isPlayable(song)) {
                if (!song.getDownloadStatus()) {
                }
                playlist.add(song);
                playlistSorted.add(song);
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
        song.like(VibeActivity.getContextOfApplication());

        viableSongs.add(song);

        // reinsert song to update its priority
        if (isPlayable(song)) {
            playlist.remove(song);
            playlist.add(song);
        }
    }

    /* Updates the status of a song if it is disliked */
    public void dislikeSong(Song song) {
        song.dislike(VibeActivity.getContextOfApplication());
        viableSongs.remove(song);
        playlist.remove(song);
    }

    /* Updates the status of a song if it is neutral */
    public void neutralSong(Song song) {
        song.neutral(VibeActivity.getContextOfApplication());
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
        return song.getSongStatus(VibeActivity.getContextOfApplication());
    }

    /* Determines whether a song is viable for playability; song must be:
        1. Not disliked
        2. Have a current and previous location/date
     */
    private boolean isPlayable(Song song) {
        return song.getSongStatus(VibeActivity.getContextOfApplication()) != -1
                && song.getDate() != null
                && song.getLocations() != null;
    }
}
