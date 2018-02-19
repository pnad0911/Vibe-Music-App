package cse_110.flashback_player;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void getScore_isCorrect() throws Exception {
        Song song = new Song("My Song", "Beverly", "My Album");
        song.setPreviousDate(OffsetDateTime.now());
        song.setCurrentDate(OffsetDateTime.of(2018, 2, 16, 16, 3, 30, 100, ZoneOffset(-08:00));
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
        Location myLoc = new Location(LocationManager.NETWORK_PROVIDER);
        myLoc.setLatitude(100);
        System.out.println(myLoc.getLatitude());
        myLoc.setLongitude(100);
        System.out.println(myLoc.getLongitude());

        song.setPreviousLocation(myLoc);
        //assertEquals(song.getLocationScore(myLoc), 100, .01);
        myLoc.setLatitude(166.0001);
        assertEquals(song.getLocationScore(myLoc), 96.3472, .0001);/*
        myLoc.setLongitude(.1);
        song.setCurrentLocation(myLoc);
        //assertEquals(song.getLocationScore(), 0, .0001);
        myLoc.setLatitude(0);
        myLoc.setLongitude(.003);
        song.setCurrentLocation(myLoc);
        //assertEquals(song.getLocationScore(), 8.6275, 0.0001);*/
    }
}
