<<<<<<< HEAD
//package tests;
//
//import android.support.test.rule.ActivityTestRule;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//
//import java.time.OffsetDateTime;
//
//import cse_110.flashback_player.NormalActivity;
//import cse_110.flashback_player.Song;
//import static org.junit.Assert.*;
//
///**
// * Created by winnieli on 2/18/18.
// */
//
//public class SongUnitTests {
//    Song song;
//
//    @Rule
//    public ActivityTestRule<NormalActivity> main2Activity = new ActivityTestRule<NormalActivity>(NormalActivity.class);
//
//
//    @Before
//    public void setUp(){
//        song = new Song("My Song", "Beverly", "My Album");
//    }
//
//    @Test
//    public void testGetTimeOfDay(){
//        assertEquals(song.getTimeofDay(420), "morning");
//        assertEquals(song.getTimeofDay(300), "morning");
//        assertEquals(song.getTimeofDay(1020), "afternoon");
//        assertEquals(song.getTimeofDay(1021), "evening");
//        assertEquals(song.getTimeofDay(0), "evening");
//    }
//
//    @Test
//    public void testGetTimeScore(){
//        OffsetDateTime time = OffsetDateTime.parse("2018-02-18T16:00:00+00:00");
//        song.setPreviousDate(time);
//        assertEquals(100,song.getTimeScore(time));
//
//        OffsetDateTime currentTime = OffsetDateTime.parse("2018-02-18T10:00:00+00:00");
//        song.setPreviousDate(time);
//        assertEquals(0,song.getTimeScore(currentTime));
//
//        Song emptySong = new Song("My Song","Winnieee","Album");
//        assertEquals(0,emptySong.getTimeScore(time));
//
//        assertEquals(0,song.getTimeScore(null));
//    }
//
//    @Test
//    public void getLocationScoreCorrect() {
////        Song song = new Song("My Song", "Beverly", "My Album");
////        Location myLoc = new Location(LocationManager.NETWORK_PROVIDER);
////        myLoc.setLatitude(100);
////        System.out.println(myLoc.getLatitude());
////        myLoc.setLongitude(100);
////        System.out.println(myLoc.getLongitude());
////
////        song.setPreviousLocation(myLoc);
////        //assertEquals(song.getLocationScore(myLoc), 100, .01);
////        myLoc.setLatitude(166.0001);
////        assertEquals(song.getLocationScore(myLoc), 96.3472, .0001);/*
////        myLoc.setLongitude(.1);
////        song.setCurrentLocation(myLoc);
////        //assertEquals(song.getLocationScore(), 0, .0001);
////        myLoc.setLatitude(0);
////        myLoc.setLongitude(.003);
////        song.setCurrentLocation(myLoc);
////        //assertEquals(song.getLocationScore(), 8.6275, 0.0001);*/
//    }
//
//}
//
//
//
//
//
//
//
//
=======
package tests;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.OffsetDateTime;

import cse_110.flashback_player.LibraryActivity;
import cse_110.flashback_player.Song;
import static org.junit.Assert.*;

/**
 * Created by winnieli on 2/18/18.
 */

public class SongUnitTests {
    Song song;

    @Rule
    public ActivityTestRule<LibraryActivity> main2Activity = new ActivityTestRule<LibraryActivity>(LibraryActivity.class);


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

    @Test
    public void getLocationScoreCorrect() {
//        Song song = new Song("My Song", "Beverly", "My Album");
//        Location myLoc = new Location(LocationManager.NETWORK_PROVIDER);
//        myLoc.setLatitude(100);
//        System.out.println(myLoc.getLatitude());
//        myLoc.setLongitude(100);
//        System.out.println(myLoc.getLongitude());
//
//        song.setPreviousLocation(myLoc);
//        //assertEquals(song.getLocationScore(myLoc), 100, .01);
//        myLoc.setLatitude(166.0001);
//        assertEquals(song.getLocationScore(myLoc), 96.3472, .0001);/*
//        myLoc.setLongitude(.1);
//        song.setCurrentLocation(myLoc);
//        //assertEquals(song.getLocationScore(), 0, .0001);
//        myLoc.setLatitude(0);
//        myLoc.setLongitude(.003);
//        song.setCurrentLocation(myLoc);
//        //assertEquals(song.getLocationScore(), 8.6275, 0.0001);*/
    }

}








>>>>>>> 3fcb2be4fa4db1edd39a6d6dcdbfbcfa65618d7e
