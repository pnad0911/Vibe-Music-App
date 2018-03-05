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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;

import cse_110.flashback_player.Friend;
import cse_110.flashback_player.NormalActivity;
import cse_110.flashback_player.Song;
import static org.junit.Assert.*;

/**
 * Created by Yutong on 3/1/18.
 */

public class FriendMoveTest {

    Song song;
    Song song2;
    Friend friend;
    Friend friend2;
    Location location1;
    Location location2;
    Long time1;
    Long time2;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef = database.getReference();

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

        time1 = (long) 123456789;
        time2 = (long) 349857203;

        friend = new Friend("abc", location1, time1 );
        friend2 = new Friend("def", location2, time2 );
        song = new Song("aaa",123,"asd","asdf",friend);
        song2 = new Song("bbb", 234, "asd","asdf", friend2);
    }

    @Test
    public void testSongConstructor(){

        assertEquals(song.getLatitude(), location1.getLatitude(),0);
        assertEquals(song.getCurrentDate().toString(), time1.toString());

        DatabaseReference songRef = databaseRef.child("SONGS").getRef();

        song.addUser("def");

        Query queryRef = songRef.orderByChild("title").equalTo("aaa");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot == null || snapshot.getValue() == null){
                    Log.println(Log.DEBUG, "info", "No such song as aaa");

                }
                else {
                    Log.println(Log.DEBUG, "info", snapshot.child("aaa").child("userNames").getValue().toString());

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
