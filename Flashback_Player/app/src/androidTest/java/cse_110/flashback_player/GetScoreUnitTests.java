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
                LocalDateTime.of(2018, 02, song.getPreviousDate().getDayOfMonth() - 1, 9, 50),
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
        song.setPreviousLocation(myLoc);
        assertEquals(song.getLocationScore(myLoc), 100, .01);

        myLoc.setLatitude(166.0001);
        myLoc.setLongitude(134.45);
        song.setPreviousLocation(myLoc);
        Location newLoc = new Location(locationProvider);
        newLoc.setLatitude(166);
        newLoc.setLongitude(134.45);
        song.setCurrentLocation(myLoc);
        assertEquals(song.getLocationScore(newLoc), 96.3477, .0001);/*
        myLoc.setLongitude(.1);
        song.setCurrentLocation(myLoc);
        //assertEquals(song.getLocationScore(), 0, .0001);
        myLoc.setLatitude(0);
        myLoc.setLongitude(.003);
        song.setCurrentLocation(myLoc);
        //assertEquals(song.getLocationScore(), 8.6275, 0.0001);*/
    }
}
