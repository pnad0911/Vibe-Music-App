package tests;

import android.location.Location;
import android.location.LocationManager;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import cse_110.flashback_player.Main2Activity;
import cse_110.flashback_player.Song;
import static org.junit.Assert.*;

/**
 * Created by winnieli on 2/18/18.
 */

public class SongUnitTests {
    Song song;

    @Rule
    public ActivityTestRule<Main2Activity> main2Activity = new ActivityTestRule<Main2Activity>(Main2Activity.class);


    @Before
    public void setUp(){
        song = new Song("My Song", "Beverly", "My Album");
    }

    @Test
    public void testGetTimeOfDay(){
        assertEquals(song.getTimeofDay(420), "morning");
        assertEquals(song.getTimeofDay(300), "morning");
        assertEquals(song.getTimeofDay(1020), "afternoon");
        assertEquals(song.getTimeofDay(1021), "evening");
        assertEquals(song.getTimeofDay(0), "evening");
    }

    @Test
    public void testGetTimeScore(){
        OffsetDateTime time = OffsetDateTime.parse("2018-02-18T16:00:00+00:00");
        song.setPreviousDate(time);
        assertEquals(100,song.getTimeScore(time));

        OffsetDateTime currentTime = OffsetDateTime.parse("2018-02-18T10:00:00+00:00");
        song.setPreviousDate(time);
        assertEquals(0,song.getTimeScore(currentTime));

        Song emptySong = new Song("My Song","Winnieee","Album");
        assertEquals(0,emptySong.getTimeScore(time));

        assertEquals(0,song.getTimeScore(null));
    }


}








