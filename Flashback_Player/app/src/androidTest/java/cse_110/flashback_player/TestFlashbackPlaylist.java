package cse_110.flashback_player;

import android.location.Location;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static android.location.LocationManager.GPS_PROVIDER;
import static org.junit.Assert.*;

/**
 * Tests Flashback Playlist
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestFlashbackPlaylist {

    private FlashbackPlaylist playlist = new FlashbackPlaylist();

    @Rule
    public ActivityTestRule<Main2Activity> mainActivity = new ActivityTestRule<Main2Activity>(MainActivity.class);

    Song song1 = new Song("test1", R.raw.after_the_storm, "artist", "album");
    Song song2 = new Song("test2", R.raw.america_religious, "artist", "album");
    Song song3 = new Song("test3", R.raw.at_midnight, "artist", "album");

    OffsetDateTime dummyT1 = OffsetDateTime.parse("2007-12-03T10:15:30+01:00");
    OffsetDateTime dummyT2 = OffsetDateTime.parse("2007-12-03T10:15:30+01:00");
    OffsetDateTime dummyT3 = OffsetDateTime.parse("2007-12-03T10:15:30+01:00");

    Location dummyL1 = new Location(GPS_PROVIDER);
    Location dummyL2 = new Location(GPS_PROVIDER);
    Location dummyL3 = new Location(GPS_PROVIDER);

    @Before
    public void setUp(){
        dummyL1.setLatitude(100);
        dummyL1.setLongitude(100);

        dummyL2.setLatitude(100);
        dummyL2.setLongitude(100);

        dummyL3.setLatitude(100);
        dummyL3.setLongitude(100);

        song1.setCurrentLocation(dummyL1);
        song1.setPreviousDate(dummyT1);

        song2.setCurrentLocation(dummyL2);
        song2.setPreviousDate(dummyT2);

        song3.setCurrentLocation(dummyL3);
        song3.setPreviousDate(dummyT3);
    }

    @Test
    /* Test if only the three songs here are in the playlist */
    public void testGetPlaylist(){
        assertEquals(playlist.getPlaylist().size(), 3);
        assertEquals(playlist.getPlaylist().contains(song1), true);
        ssertEquals(playlist.getPlaylist().contains(song2), true);
        ssertEquals(playlist.getPlaylist().contains(song3), true);
    }

    @Test
    /* Testing if liked songs are of high priority*/
    public void testLikedSong(){
        playlist.likeSong(song1);

        assertEquals(song1.getLikedStatus(),true);
        assertEquals(playlist.getPlaylist().get(0).getID(), song1.getID());
    }

    @Test
    /* Testing if disliked songs are no longer in the playlist*/
    public void testDislikedSong(){


        assertEquals(song1.getLikedStatus(),false);
        assertEquals(song1.getPlaylist().contains(song1), false);
    }

    @Test
    /* Testing if neutral songs are neutral (put back into the playlist */
    public void testNeutralSong(){
        playlist.neutralSong(song1);

        assertEquals(song1.getLikedStatus(),null);
        assertEquals(song1.getPlaylist().contains(song1), true);
    }
}