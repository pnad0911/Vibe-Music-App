package cse_110.flashback_player;

import android.content.Context;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Daniel on 3/13/2018.
 */

public class SongListSorter {

    /* Sort songs in the playlist by lexicographic order according to title */
    public List<Song> sortByTitle(List<Song> songList) {
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song song1, Song song2) {
                return song1.getTitle().compareTo(song2.getTitle());
            }
        });

        return songList;
    }

    /* Sort songs in the playlist by lexicographic order according to album */
    public List<Song> sortByAlbum(List<Song> songList) {
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song song1, Song song2) {
                return song1.getAlbum().compareTo(song2.getAlbum());
            }
        });

        return songList;
    }

    /* Sort songs in the playlist by lexicographic order according to artist */
    public List<Song> sortByArtist(List<Song> songList) {
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song song1, Song song2) {
                return song1.getArtist().compareTo(song2.getArtist());
            }
        });

        return songList;
    }
    /*
    /* Sort songs in the playlist by lexicographic order according to favorite status
    public List<Song> sortByStatus(List<Song> songList, final Context context) {
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song song1, Song song2) {
                if (song1.getSongStatus(context) > (song2.getSongStatus(context))) {
                    return 1;
                } else if (song1.getSongStatus(context) < song2.getSongStatus(context)) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }
    */

    /* Sort songs in the playlist by lexicographic order according to favorite status
* */
    public List<Song> sortByStatus (List<Song> songList) {
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song song1, Song song2) {
                if (song1.getStatus() < (song2.getStatus())) {
                    return 1;
                } else if (song1.getStatus() > song2.getStatus()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return songList;
    }
}
