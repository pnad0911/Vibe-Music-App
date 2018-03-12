
//package cse_110.flashback_player;
//
//import android.content.Context;
//import android.location.Location;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.proto.action.ViewActions;
//import android.support.test.runner.AndroidJUnit4;
//import android.util.Log;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.time.OffsetDateTime;
//
//import static android.content.ContentValues.TAG;
//import static android.location.LocationManager.GPS_PROVIDER;
//import static org.junit.Assert.*;
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//@RunWith(AndroidJUnit4.class)
//public class TestSongCompare {
//    private Location loc = new Location(GPS_PROVIDER);
//    private OffsetDateTime time = OffsetDateTime.parse("2007-12-03T10:15:30+01:00");
//    private Song song1;
//    private Song song2;
//    private SongCompare songCompare;
//    @Before
//    public void setup() {
//        loc.setLongitude(100);loc.setLatitude(100);
//        song1 = new Song("a","a", "a");
//        song2 = new Song("a","a", "a");
//        try {
//            songCompare = new SongCompare(loc, time);
//        } catch(NullPointerException e) {
//            Log.i(TAG,"null pointer");
//        }
//    }
//    @Test
//    public void test1() {
//        assertEquals(0,songCompare.compare(song1,song2));
//    }
//}
