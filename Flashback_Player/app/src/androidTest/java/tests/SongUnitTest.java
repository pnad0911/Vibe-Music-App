package tests;

import org.junit.Rule;

import android.location.Location;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cse_110.flashback_player.Database;
import cse_110.flashback_player.Friend;
import cse_110.flashback_player.LibraryActivity;
import cse_110.flashback_player.Song;
import static org.junit.Assert.*;

/**
 * Created by Yutong on 3/1/18.
 */

public class SongUnitTest {

    Song song;
    Song song2;
    Friend friend;
    Friend friend2;
    Location location1;
    Location location2;
    OffsetDateTime time1;
    OffsetDateTime time2;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef = database.getReference();

    private GenericTypeIndicator<ArrayList<HashMap<String,String>>> t = new GenericTypeIndicator<ArrayList<HashMap<String,String>>>() {};
    private GenericTypeIndicator<ArrayList<String>> n = new GenericTypeIndicator<ArrayList<String>>() {};

//    @Rule
//    public ActivityTestRule<LibraryActivity> main2Activity = new ActivityTestRule<LibraryActivity>(LibraryActivity.class);

    @Before
    public void setUp(){
        location1 = new Location(LocationManager.GPS_PROVIDER);
        location1.setLatitude(1000);
        location1.setLongitude(1000);

        location2 = new Location(LocationManager.GPS_PROVIDER);
        location2.setLatitude(2000);
        location2.setLongitude(1000);

        time1 = OffsetDateTime.of(2000,10,10,10,10,10,10, ZoneOffset.UTC);
        time2 = OffsetDateTime.of(2010,11,12,10,10,10,10, ZoneOffset.UTC);

//        friend = new Friend("abc", location1, time1 );
//        friend2 = new Friend("def", location2, time2 );
        song = new Song("aaa", "asd","123","asdf",false);
        song.addUser("abc","abc");
        song.addLocation(location1);
        song.setDate(time1);
        Database.updateDatabase(song);

        song2 = new Song("bbb", "asd", "234","asdf",false);
        song2.addUser("def","def");
        song2.addLocation(location2);
        song2.setDate(time2);
        Database.updateDatabase(song2);
    }

    @Test
    public void testSongConstructor(){

        assertEquals(song.allLocations().get(0).first, Float.toString((float)location1.getLongitude()));
        assertEquals(song.getDate(), time1.toString());

    }

    @Test
    public void testSongConstructorDatabase(){
        Log.e("testSongConstructorDatabase", "-------------------");

        Song newSong = new Song("bbb", "asd","234","asdfgg",false);
        Database.loadSong(newSong);
        try{ Thread.sleep(1000); } catch (Exception e){ e.printStackTrace();} //wait for data
        assertEquals(time2.toString(), newSong.getDate());
    }

    @Test
    public void testUpdate(){
        Log.e("testUpdate", "-------------------");

        song.addUser("def","def");
        Database.updateDatabase(song);

        song.addUser("egr","egr");
        Database.updateDatabase(song);

        Song newSong = new Song();
        newSong.setDatabaseKey(song.getDatabaseKey());
        Log.e("testUpdate", song.getDatabaseKey());
        Log.e("testUpdate", newSong.getDatabaseKey());

        Database.loadSong(newSong);
        assertEquals(song.getUserNames().get(1), "defdef");
    }

//    @Test
//    public void testFriendMove(){
//        friend.setLocation(location2);
//        assertEquals(song.getCurrentLocation().getLatitude(), location2.getLatitude(),0);
//    }
//
//    @Test
//    public void testFriendChangeTime(){
//        friend.setTime(time2);
//        assertEquals(song.getCurrentDate().toString(), time2.toString());
//    }


}
