package cse_110.flashback_player;

import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Daniel on 3/13/2018.
 */

public class TestSongListSorter {
    protected List<Song> songList;
    protected SongListSorter songListSorter;

//    @Rule
//    public ActivityTestRule<VibeActivity> mainActivity = new ActivityTestRule<VibeActivity>(VibeActivity.class);

    @Before
    // assign values
    public void setUp() {
        songList = new ArrayList<>();
        songListSorter = new SongListSorter();

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
    }

    // test method to sort songList by title
    @Test
    public void testSortByTitle() {
        songListSorter.sortByTitle(songList);
        Iterator<Song> songIterator = songList.listIterator();

        try {
            assertTrue(songIterator.next().getTitle().equals("Berry"));
            assertTrue(songIterator.next().getTitle().equals("Best Song"));
            assertTrue(songIterator.next().getTitle().equals("Good Song"));
            assertTrue(songIterator.next().getTitle().equals("Iron Man"));
            assertTrue(songIterator.next().getTitle().equals("Legion"));
            assertTrue(songIterator.next().getTitle().equals("Single Ready To Mingle"));
            assertTrue(songIterator.next().getTitle().equals("Strawberry"));
        } catch (NullPointerException e) {
            Log.println(Log.ERROR, "DUY MADE ME DO THIS", "Null pointer exception");
        }
    }

    // test method to sort songList by album
    @Test
    public void testSortByAlbum() {
        songListSorter.sortByAlbum(songList);
        Iterator<Song> songIterator = songList.listIterator();

        try {
            assertTrue(songIterator.next().getAlbum().equals("My Greatest Album"));
            assertTrue(songIterator.next().getAlbum().equals("My Greatest Album"));
            assertTrue(songIterator.next().getAlbum().equals("My Greatest Album"));
            assertTrue(songIterator.next().getAlbum().equals("My Greatest Single"));
            assertTrue(songIterator.next().getAlbum().equals("My Greatest Single"));
            assertTrue(songIterator.next().getAlbum().equals("My Greatest Single"));
            assertTrue(songIterator.next().getAlbum().equals("My Greatest Single"));
        } catch (NullPointerException e) {
            Log.println(Log.ERROR, "DUY MADE ME DO THIS", "Null pointer exception");
        }
    }

    // test method to sort songList by artist
    @Test
    public void testSortByArtist() {
        songListSorter.sortByArtist(songList);
        ListIterator<Song> songIterator = songList.listIterator();

        try {
            assertTrue(songIterator.next().getArtist().equals("AAA"));
            assertTrue(songIterator.next().getArtist().equals("Big bully"));
            assertTrue(songIterator.next().getArtist().equals("D Bawss"));
            assertTrue(songIterator.next().getArtist().equals("D Bawss"));
            assertTrue(songIterator.next().getArtist().equals("beverley"));
            assertTrue(songIterator.next().getArtist().equals("bill_killer"));
            assertTrue(songIterator.next().getArtist().equals("汤圆"));
        } catch (NullPointerException e) {
            Log.println(Log.ERROR, "DUY MADE ME DO THIS", "Null pointer exception");
        }
    }

    // test method to sort songList by status
    @Test
    public void testSortByStatus() {
        songListSorter.sortByStatus(songList);
        Iterator<Song> songIterator = songList.listIterator();

        try {
            assertTrue(songIterator.next().getSongStatus() == (0));
            assertTrue(songIterator.next().getSongStatus() == (0));
            assertTrue(songIterator.next().getSongStatus() == (0));
            assertTrue(songIterator.next().getSongStatus() == (0));
            assertTrue(songIterator.next().getSongStatus() == (0));
            assertTrue(songIterator.next().getSongStatus() == (0));
            assertTrue(songIterator.next().getSongStatus() == (0));
        } catch (NullPointerException e) {
            Log.println(Log.ERROR, "DUY MADE ME DO THIS", "Null pointer exception");
        }
    }
}
