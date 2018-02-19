package cse_110.flashback_player;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import cse_110.flashback_player.Song;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GetScoreUnitTests {
    @Test
    public void getTimeOfDay_isCorrect() throws Exception {
        Song song = new Song("My Song", "Beverly", "My Album");

        assertEquals(song.getTimeofDay(330), "morning");
        assertEquals(song.getTimeofDay(500), "morning");
        assertEquals(song.getTimeofDay(660), "morning");
        assertEquals(song.getTimeofDay(661), "afternoon");
        assertEquals(song.getTimeofDay(1020), "afternoon");
        assertEquals(song.getTimeofDay(1021), "evening");
        assertEquals(song.getTimeofDay(0), "evening");

    }

    @Test
    public void getTimeScore_isCorrect() throws Exception {
        Song song = new Song("My Song", "Beverly", "My Album");
        OffsetDateTime present = OffsetDateTime.now();
        song.setPreviousDate(present);
        assertEquals(song.getTimeScore(present), 100, .01);
        song.setPreviousDate(OffsetDateTime.of(
                LocalDateTime.of(2018, 02, 17, 9, 50),
                ZoneOffset.ofHoursMinutes(12 - present.getHour() ,0)));
        assertEquals(song.getTimeScore(present), 0, .01);
    }

    @Test
    public void getDateScoreCorrect() throws Exception {
        Song song = new Song("My Song", "Beverly", "My Album");
        OffsetDateTime present = OffsetDateTime.now();
        song.setPreviousDate(present);
        assertEquals(song.getDateScore(present), 100, .01);
        song.setPreviousDate(OffsetDateTime.of(
                LocalDateTime.of(2018, 02, song.getCurrentDate().getDayOfMonth() - 1, 9, 50),
                ZoneOffset.ofHoursMinutes(-8,0)));
        assertEquals(song.getDateScore(present), 0, .01);
    }

    @Test
    public void getLocationScoreCorrect() {
        Song song = new Song("My Song", "Beverly", "My Album");
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location myLoc = new Location(locationProvider);

        myLoc.setLatitude(0);
        myLoc.setLongitude(0);
        song.setPreviousLocationShared(myLoc);
        assertEquals(song.getLocationScore(myLoc), 100, .01);

        myLoc.setLatitude(166.0001);
        myLoc.setLongitude(134.45);
        song.setPreviousLocationShared(myLoc);
        myLoc.setLatitude(166);
        myLoc.setLongitude(134.45);
        assertEquals(song.getLocationScore(myLoc), 96.3477, .0001);

        myLoc.setLongitude(1);
        assertEquals(song.getLocationScore(myLoc), 0, .0001);

        myLoc.setLatitude(0);
        myLoc.setLongitude(.003);
        song.setPreviousLocationShared(myLoc);
        myLoc.setLatitude(0);
        myLoc.setLongitude(0);
        assertEquals(song.getLocationScore(myLoc), 8.2675, 0.0001);
    }
}
