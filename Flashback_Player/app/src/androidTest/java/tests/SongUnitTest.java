package tests;

import org.junit.Rule;

import android.location.Location;
import android.location.LocationManager;
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

    @Rule
    public ActivityTestRule<LibraryActivity> main2Activity = new ActivityTestRule<LibraryActivity>(LibraryActivity.class);

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
        song.updateDatabase();
        song2 = new Song("bbb", "asd", "234","asdf",false);
        song2.addUser("def","def");
        song2.addLocation(location2);
        song2.setDate(time2);
        song2.updateDatabase();
    }

    @Test
    public void testSongConstructor(){

     //   assertEquals(song.getLocations().get(0).first, Integer.toString((int)location1.getLongitude()));

        assertEquals(song.getDate(), time1.toString());

    }

    @Test
    public void testSongConstructorDatabase(){

        Song newSong = new Song("aaa", "asd","234","asdfgg",false);
        Log.println(Log.ERROR, "TESING", "New date: " + newSong.getDate());

       // assertEquals("1000", newSong.getLocations().get(0).first);

    }

    @Test
    public void testUpdate(){

        song.addUser("def","def");
        song.updateDatabase();

        song.addUser("egr","egr");
        song.updateDatabase();

        Query queryRef = databaseRef.child("SONGS").orderByChild("databaseKey").equalTo("aaaasd");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot == null || snapshot.getValue() == null){
                    Log.println(Log.DEBUG, "info", "No such song as aaa");
                    fail("Did not find the song");

                }
                else {
                    Log.println(Log.ERROR, "info", "Found Song: " + snapshot.child(song.getDatabaseKey()).getValue(Song.class).toString());

         //           assertEquals(snapshot.child(song.getDatabaseKey()).child("userNames").getValue(t).get(0), "defdef");

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Faile to read value
                Log.w("TAG1", "Failed to read value.", error.toException());
            }
        });
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
