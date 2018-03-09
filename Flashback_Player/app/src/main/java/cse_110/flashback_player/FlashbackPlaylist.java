
package cse_110.flashback_player;

import android.app.Activity;
import android.content.Context;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Daniel on 2/17/2018.
 */

public class FlashbackPlaylist {

    /* Entire list of songs */
    private SongList entireSongList;

    /* List of viable songs to be placed in the playlist (played b4, not disliked) */
    private HashSet<Song> viableSongs;

    /* Priority queue used to build the playlist */
    private PriorityQueue<Song> playlist;

    /* Context provided by NormalActivity */
    private Context context;

    private OffsetDateTime currentTime;

    /* Constructor */
    public FlashbackPlaylist(Activity a) {
        entireSongList = new SongList(a);

        // initialize set of viable songs
        viableSongs = new HashSet<>();

        // initialize context
        context =  NormalActivity.getContextOfApplication();

        // populate viable song set
        for (Song song : entireSongList.getAllsong()) {
//            song.getPreviousDate(context);
//            song.getPreviousLocation(context);
//
//            System.out.println(song.getPreviousLocation(context));

            // song must be:
            // 1. not disliked
            // 2. having valid previous and current locations
            // 3. having a valid date
            if (isPlayable(song)) {
                viableSongs.add(song);
            }
        }
    }

    /* Update and return a list of songs in the priority queue based on a location/time */
    public List<Song> getFlashbackSong() {
        currentTime = OffsetDateTime.now().minusHours(8);

        // build priority queue
        playlist = new PriorityQueue<>(1, new SongCompare<>(VibeActivity.getLocation(), currentTime));

        // populate playlist based on new data
        for (Song song : viableSongs) {
            if (isPlayable(song)) {
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

        System.out.println("------------");
        System.out.println(returnList.size());

        return returnList;
    }

    /* Updates the status of a song if it is favorited */
    public void likeSong(Song song) {
        song.like(VibeActivity.getContextOfApplication());

        viableSongs.add(song);

        // reinsert song to SongFriendMediator its priority
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
        System.out.println(isPlayable(song));
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
        return song.getSongStatus(FlashBackActivity.getContextOfApplication()) != -1;
//                && song.getPreviousLocation(context) != null
//                && song.getPreviousDate(context) != null;
    }
}