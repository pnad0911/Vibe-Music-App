package tests;

import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cse_110.flashback_player.LibraryActivity;
import cse_110.flashback_player.Song;
import cse_110.flashback_player.SongListSorter;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Daniel on 3/13/2018.
 */

public class SongListSorterUnitTests {
    protected List<Song> songList;
    protected SongListSorter songListSorter;

    @Rule
    public ActivityTestRule<LibraryActivity> mainActivity = new ActivityTestRule<LibraryActivity>(LibraryActivity.class);

    @Before
    // assign values
    protected void setUp() {
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
            assertTrue(songIterator.next().getTitle().equals("My Greatest Album"));
            assertTrue(songIterator.next().getTitle().equals("My Greatest Album"));
            assertTrue(songIterator.next().getTitle().equals("My Greatest Album"));
            assertTrue(songIterator.next().getTitle().equals("My Greatest Single"));
            assertTrue(songIterator.next().getTitle().equals("My Greatest Single"));
            assertTrue(songIterator.next().getTitle().equals("My Greatest Single"));
            assertTrue(songIterator.next().getTitle().equals("My Greatest Single"));
        } catch (NullPointerException e) {
            Log.println(Log.ERROR, "DUY MADE ME DO THIS", "Null pointer exception");
        }
    }

    // test method to sort songList by artist
    @Test
    public void testSortByArtist() {
        songListSorter.sortByArtist(songList);
        Iterator<Song> songIterator = songList.listIterator();

        try {
            assertTrue(songIterator.next().getTitle().equals("AAA"));
            assertTrue(songIterator.next().getTitle().equals("beverley"));
            assertTrue(songIterator.next().getTitle().equals("Big bully"));
            assertTrue(songIterator.next().getTitle().equals("bill_killer"));
            assertTrue(songIterator.next().getTitle().equals("D Bawss"));
            assertTrue(songIterator.next().getTitle().equals("汤圆"));
        } catch (NullPointerException e) {
            Log.println(Log.ERROR, "DUY MADE ME DO THIS", "Null pointer exception");
        }
    }

    // test method to sort songList by status
    @Test
    public void testSortByStatus() {
        songListSorter.sortByStatus(songList, LibraryActivity.getContextOfApplication());
        Iterator<Song> songIterator = songList.listIterator();

        try {
            assertTrue(songIterator.next().getTitle().equals("Berry"));
            assertTrue(songIterator.next().getTitle().equals("Strawberry"));
            assertTrue(songIterator.next().getTitle().equals("Iron Man"));
            assertTrue(songIterator.next().getTitle().equals("Legion"));
            assertTrue(songIterator.next().getTitle().equals("Single Ready To Mingle"));
            assertTrue(songIterator.next().getTitle().equals("Good Song"));
            assertTrue(songIterator.next().getTitle().equals("Best Song"));
        } catch (NullPointerException e) {
            Log.println(Log.ERROR, "DUY MADE ME DO THIS", "Null pointer exception");
        }
    }
}
