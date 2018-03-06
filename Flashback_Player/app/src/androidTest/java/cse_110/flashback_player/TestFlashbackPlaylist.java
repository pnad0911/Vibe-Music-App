package cse_110.flashback_player;

import android.content.Context;
import android.location.Location;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.OffsetDateTime;
import java.util.List;

import static android.location.LocationManager.GPS_PROVIDER;
import static org.junit.Assert.*;

/**
 * Tests Flashback Playlist
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestFlashbackPlaylist {

    @Rule
    public ActivityTestRule<NormalActivity> mainActivity = new ActivityTestRule<NormalActivity>(NormalActivity.class);

    private FlashbackPlaylist playlist;

    /* Context provided by NormalActivity */
    private Context context;

    Song song1;
    Song song2;
    Song song3;

    OffsetDateTime dummyT1;
    OffsetDateTime dummyT2;
    OffsetDateTime dummyT3;

    Location dummyL1;
    Location dummyL2;
    Location dummyL3;

    List<Song> list;

    @Before
    public void setUp(){
        playlist = new FlashbackPlaylist();

        song1 = new Song("cant_find_love", R.raw.cant_find_love, "artist", "album");
        song2 = new Song("america_religious", R.raw.america_religious, "artist", "album");
        song3 = new Song("at_midnight", R.raw.at_midnight, "artist", "album");

        // initialize context
        context =  NormalActivity.getContextOfApplication();

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

        song1.addLocation(dummyL1);
        song1.setDate(dummyT1);

        song2.addLocation(dummyL2);
        song2.setDate(dummyT2);

        song3.addLocation(dummyL3);
        song3.setDate(dummyT3);

        playlist.neutralSong(song1);
        playlist.neutralSong(song2);
        playlist.neutralSong(song3);

        list = playlist.getFlashbackSong();
        System.out.println(list.size());
    }

        /* Test if only the three songs here are in the playlist */

    @Test
    public void testGetPlaylist(){
        assertEquals(list.size(), 3);
        assertEquals(list.contains(song1), true);
        assertEquals(list.contains(song2), true);
        assertEquals(list.contains(song3), true);
    }

    @Test
    /* Testing if liked songs are of high priority*/
    public void testLikedSong(){
        playlist.likeSong(song1);

        assertEquals(song1.getSongStatus(context),1);
        assertEquals(playlist.getFlashbackSong().get(0).getID(), song1.getID());
    }

    @Test
    /* Testing if disliked songs are no longer in the playlist*/
    public void testDislikedSong(){
        playlist.dislikeSong(song1);

        assertEquals(song1.getSongStatus(context),-1);
        assertEquals(playlist.getFlashbackSong().contains(song1), false);
    }

    @Test
    /* Testing if neutral songs are neutral (put back into the playlist */
    public void testNeutralSong(){
        playlist.neutralSong(song1);

        assertEquals(song1.getSongStatus(context),0);
        assertEquals(list.contains(song1), true);
    }
}