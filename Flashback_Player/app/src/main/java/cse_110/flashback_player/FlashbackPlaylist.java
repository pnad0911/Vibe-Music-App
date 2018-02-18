package cse_110.flashback_player;

import android.location.Location;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Daniel on 2/17/2018.
 */

public class FlashbackPlaylist {

    /* Entire list of songs in the app */
    private SongList entireSongList;

    /* List of viable songs to be placed in the playlist (played b4, not disliked) */
    private HashSet<Song> viableSongs;

    /* Priority queue used to build the playlist */
    private PriorityQueue<Song> playlist;

    /* Current location playlist is based on */
    private Location location;

    /* Current time playlist is based on */
    private OffsetDateTime date;

    /* Constructor */
    public FlashbackPlaylist(Location location, OffsetDateTime date) {
        // get entire song list
        entireSongList = new SongList();

        // initialize set of viable songs
        viableSongs = new HashSet<>();

        // populate viable song set
        for (Song song : entireSongList.getAllsong()) {
            // song must be:
            // 1. not disliked
            // 2. having valid previous and current locations
            // 3. having a valid date
            if (!song.getIsDisliked()
                    && song.getPreviousLocation() != null
                    && song.getCurrentLocation() != null
                    && song.getCurrentDate() != null) {
                viableSongs.add(song);
            }
        }

        // initialize location and date
        this.location = location;
        this.date = date;

        // build priority queue
        playlist = new PriorityQueue<>(1, new SongCompare<>());
    }

    /* Update and return a list of songs in the priority queue based on a location/time */
    public List<Song> getFlashbackSong(Location newLocation, OffsetDateTime newDate) {
        // update current location and date
        location = newLocation;
        date = newDate;

        // populate playlist based on new data
        playlist.clear();
        for (Song song : viableSongs) {
            song.setCurrentLocation(location);
            song.setCurrentDate(date);
            if (song.getScore() > 0) {
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

    /* Updates the status of a song if it is liked */
    public void likeSong(Song song) {
        viableSongs.add(song);

        // reinsert song to update its priority
        if (song.getScore() > 0) {
            playlist.remove(song);
            playlist.add(song);
        }
    }

    /* Updates the status of a song if it is disliked */
    public void dislikeSong(Song song) {
        viableSongs.remove(song);
        playlist.remove(song);
    }

    /* Updates the status of a song if it is neutral */
    public void neutralSong(Song song) {
        viableSongs.add(song);
        if (!playlist.contains(song) && song.getScore() > 0) {
            playlist.add(song);
        }
    }

    /* Returns whether or not the song is currently favorited */
    public boolean songCurrentlyLiked(Song song) {
        return song.getIsFavorite();
    }
}
