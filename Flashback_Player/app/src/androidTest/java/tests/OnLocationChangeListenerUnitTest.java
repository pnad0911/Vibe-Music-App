package tests;

import org.junit.Rule;

import android.location.Location;
import android.location.LocationManager;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import cse_110.flashback_player.Friend;
import cse_110.flashback_player.NormalActivity;
import cse_110.flashback_player.Song;
import static org.junit.Assert.*;

/**
 * Created by Yutong on 3/1/18.
 */

public class OnLocationChangeListenerUnitTest {

    Song song;
    Friend friend;
    Location location1;
    Location location2;
    OffsetDateTime time1;
    OffsetDateTime time2;

    @Rule
    public ActivityTestRule<NormalActivity> main2Activity = new ActivityTestRule<NormalActivity>(NormalActivity.class);


    @Before
    public void setUp(){
        location1 = new Location(LocationManager.GPS_PROVIDER);
        location1.setLatitude(1000);
        location1.setLongitude(1000);

        location2 = new Location(LocationManager.GPS_PROVIDER);
        location2.setLatitude(2000);
        location2.setLongitude(1000);

        time1 = OffsetDateTime.of(LocalDateTime.of(2017, 05, 12, 05, 45), ZoneOffset.ofHoursMinutes(6, 30));
        time2 = OffsetDateTime.of(LocalDateTime.of(2018, 12, 12, 05, 45), ZoneOffset.ofHoursMinutes(6, 30));

        friend = new Friend("abc", location1, time1 );
        song = new Song("aaa",123,"asd","asdf",friend);
    }

    @Test
    public void testSongConstructor(){
        friend = new Friend("abc", location1, time1 );
        song = new Song("aaa",123,"asd","asdf",friend);
        assertEquals(song.getCurrentLocation().getLatitude(), location1.getLatitude(),0);
        assertEquals(song.getCurrentDate().toString(), time1.toString());
    }

    @Test
    public void testFriendMove(){
        friend.setLocation(location2);
        assertEquals(song.getCurrentLocation().getLatitude(), location2.getLatitude(),0);
    }

    @Test
    public void testFriendChangeTime(){
        friend.setTime(time2);
        assertEquals(song.getCurrentDate().toString(), time2.toString());
    }


}
