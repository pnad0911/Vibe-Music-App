package cse_110.flashback_player;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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

    /* Sort songs in the playlist by lexicographic order according to favorite status */
    public List<Song> sortByStatus(List<Song> songList, final Context context) {
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song song1, Song song2) {
                if (song1.getSongStatus(context) > (song2.getSongStatus(context))) {
                    return 1;
                }
                else if (song1.getSongStatus(context) < song2.getSongStatus(context)) {
                    return -1;
                }
                else {
                    return 0;
                }
            }
        });

        return songList;
    }
    public static void main(String[] args) {
        System.out.println("im ugly");
        List<Song> songList = new ArrayList<>();
        SongListSorter songListSorter = new SongListSorter();

        // artists
        String patrick = "bill_killer";
        String duy = "D Bawss";
        String yutong = "AAA";
        String beverly = "beverley";
        String winnie = "Big bully";
        String daniel = "汤圆";

        // albums
        String mygreatestalbum = "My Greatest Album";
        String mygreatestsingle = "My Greatest Single";

        // titles
        String berry = "Berry";
        String strawberry = "Strawberry";
        String ironMan = "Iron Man";
        String legion = "Legion";
        String singlereadytomingle = "Single Ready To Mingle";
        String goodsong = "Good Song";
        String bestsong = "Best Song";

        // paths
        String path = "THIS IS A PATH";

        // songs
        Song berry_song = new Song(berry, patrick, mygreatestalbum, path, true);
        Song strawberry_song = new Song(strawberry, duy, mygreatestalbum, path, true);
        Song ironMan_song = new Song(ironMan, yutong, mygreatestsingle, path, true);
        Song legion_song = new Song(legion, beverly, mygreatestsingle, path, true);
        Song singlereadytomingle_song = new Song(singlereadytomingle, duy, mygreatestsingle, path, true);
        Song goodsong_song = new Song(goodsong, winnie, mygreatestalbum, path, true);
        Song bestsong_song = new Song(bestsong, daniel, mygreatestsingle, path, true);

        songList.add(berry_song);
        songList.add(strawberry_song);
        songList.add(ironMan_song);
        songList.add(legion_song);
        songList.add(singlereadytomingle_song);
        songList.add(goodsong_song);
        songList.add(bestsong_song);

        songListSorter.sortByTitle(songList);
        Iterator<Song> songIterator = songList.listIterator();

        try {
            System.out.println(songIterator.next().getTitle().equals("Berry"));
            System.out.println(songIterator.next().getTitle().equals("Best Song"));
            System.out.println(songIterator.next().getTitle().equals("Good Song"));
            System.out.println(songIterator.next().getTitle().equals("Iron Man"));
            System.out.println(songIterator.next().getTitle().equals("Legion"));
            System.out.println(songIterator.next().getTitle().equals("Single Ready To Mingle"));
            System.out.println(songIterator.next().getTitle().equals("Strawberry"));
        } catch (NullPointerException e) {
            Log.println(Log.ERROR, "DUY MADE ME DO THIS", "Null pointer exception");
        }
    }
}
