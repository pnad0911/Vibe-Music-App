package cse_110.flashback_player;

import android.location.Location;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.OffsetDateTime;

import static android.location.LocationManager.GPS_PROVIDER;
import static org.junit.Assert.*;

/**
 * Tests Flashback Playlist
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestFlashbackPlaylist {


    public TestFlashbackPlaylist(){

    }

    @Rule
    public ActivityTestRule<Main2Activity> mainActivity = new ActivityTestRule<Main2Activity>(Main2Activity.class);

    private FlashbackPlaylist playlist;

    Song song1;
    Song song2;
    Song song3;

    OffsetDateTime dummyT1;
    OffsetDateTime dummyT2;
    OffsetDateTime dummyT3;

    Location dummyL1;
    Location dummyL2;
    Location dummyL3;

    @Before
    public void setUp(){
        playlist = new FlashbackPlaylist();

        song1 = new Song("test1", R.raw.cant_find_love, "artist", "album");
        song2 = new Song("test2", R.raw.america_religious, "artist", "album");
        song3 = new Song("test3", R.raw.at_midnight, "artist", "album");

        dummyT1 = OffsetDateTime.parse("2007-12-03T10:15:30+01:00");
        dummyT2 = OffsetDateTime.parse("2007-12-03T10:15:30+01:00");
        dummyT3 = OffsetDateTime.parse("2007-12-03T10:15:30+01:00");

        dummyL1 = new Location(GPS_PROVIDER);
        dummyL2 = new Location(GPS_PROVIDER);
        dummyL3 = new Location(GPS_PROVIDER);

        dummyL1.setLatitude(100);
        dummyL1.setLongitude(100);

        dummyL2.setLatitude(100);
        dummyL2.setLongitude(100);

        dummyL3.setLatitude(100);
        dummyL3.setLongitude(100);

        song1.setPreviousLocation(dummyL1);
        song1.setPreviousDate(dummyT1);

        song2.setPreviousLocation(dummyL2);
        song2.setPreviousDate(dummyT2);

        song3.setPreviousLocation(dummyL3);
        song3.setPreviousDate(dummyT3);
    }

        /* Test if only the three songs here are in the playlist */

    @Test
    public void testGetPlaylist(){
        assertEquals(playlist.getFlashbackSong().size(), 3);
        assertEquals(playlist.getFlashbackSong().contains(song1), true);
        assertEquals(playlist.getFlashbackSong().contains(song2), true);
        assertEquals(playlist.getFlashbackSong().contains(song3), true);
    }

    @Test
    /* Testing if liked songs are of high priority*/
    public void testLikedSong(){
        playlist.likeSong(song1);

        assertEquals(song1.getSongStatus(),true);
        assertEquals(playlist.getFlashbackSong().get(0).getID(), song1.getID());
    }

    @Test
    /* Testing if disliked songs are no longer in the playlist*/
    public void testDislikedSong(){
        playlist.dislikeSong(song1);

        assertEquals(song1.getSongStatus(),false);
        assertEquals(playlist.getFlashbackSong().contains(song1), false);
    }

    @Test
    /* Testing if neutral songs are neutral (put back into the playlist */
    public void testNeutralSong(){
        playlist.neutralSong(song1);

        assertEquals(song1.getSongStatus(),0);
        assertEquals(playlist.getFlashbackSong().contains(song1), true);
    }
}