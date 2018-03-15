package cse_110.flashback_player;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yutong on 3/15/18.
 */

public class Database {

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference databaseRef = database.getReference();

    private static GenericTypeIndicator<ArrayList<HashMap<String,String>>> t = new GenericTypeIndicator<ArrayList<HashMap<String,String>>>() {};
    private static GenericTypeIndicator<ArrayList<String>> n = new GenericTypeIndicator<ArrayList<String>>() {};

    private static ArrayList<DatabaseListener> listeners = new ArrayList<>();

    /** Method called as long as this object is modified.
     *  The display is responsible to call this method when location/date/user is set.
     */
    public static void updateDatabase(Song song){
        Log.println(Log.ERROR, "songURL", "Songurl is: "+song.getSongUrl());
//        if (songUrl == null){songUrl = "";}
        Log.println(Log.ERROR, "Database", "Updating song " + song.getTitle() + " in Firebase.");
        DatabaseReference songDataRef = databaseRef.child("SONGS");

        songDataRef.child(song.getDatabaseKey()).child("title").setValue(song.getTitle());
        songDataRef.child(song.getDatabaseKey()).child("artist").setValue(song.getArtist());
        songDataRef.child(song.getDatabaseKey()).child("databaseKey").setValue(song.getDatabaseKey());
        songDataRef.child(song.getDatabaseKey()).child("album").setValue(song.getAlbum());
        songDataRef.child(song.getDatabaseKey()).child("songUrl").setValue(song.getSongUrl());

        songDataRef.child(song.getDatabaseKey()).child("userNames").setValue(song.getUserNames());
        songDataRef.child(song.getDatabaseKey()).child("locations").setValue(song.getLocations());
        songDataRef.child(song.getDatabaseKey()).child("date").setValue(song.getDate());
//
        for (DatabaseListener ob : listeners){
            ob.update(song);
        }
    }

    /**
     * Load from Database.
     * Once called, will be monitoring the database constantly and will be updating the
     *          song object once there is a change in database.
     * */
    public static void loadSong (final Song s){

        DatabaseReference songRef = databaseRef.child("SONGS").getRef();
        Query queryRef = songRef.orderByChild("databaseKey").equalTo(s.getDatabaseKey());
        queryRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getValue() == null) {
                    Log.println(Log.ERROR, "loadSong", "No such song as "+s.getDatabaseKey());

                } else {
                    Log.println(Log.ERROR, "loadSong", "Found song " + s.getDatabaseKey());

                    DataSnapshot dsp = dataSnapshot.child(s.getDatabaseKey());

                    Song song = new Song(dsp.child("title").getValue(String.class),
                            dsp.child("artist").getValue(String.class),
                            dsp.child("album").getValue(String.class),
                            dsp.child("songUrl").getValue(String.class),
                            false);

                    song.setLocations(dsp.child("locations").getValue(t));
                    song.setUserNames(dsp.child("userNames").getValue(n));
                    song.setDate(OffsetDateTime.parse(dsp.child("date").getValue(String.class)));

                    // update current song object
                    s.update(song);
                    Log.println(Log.ERROR, "loadSong", "New date: " + s.getDate());


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Fail to read value
                Log.w("DATBASEERROR", "Failed to read value.", error.toException());
            }
        });
    }

    /* Retrieve all songs from firebase */
    public static void loadAllSongs(final VibePlaylist.dataBaseListener c) {

        DatabaseReference songRef = databaseRef.child("SONGS").getRef();
        songRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {

                    Song song = new Song(dsp.child("title").getValue(String.class),
                            dsp.child("artist").getValue(String.class),
                            dsp.child("album").getValue(String.class),
                            dsp.child("songUrl").getValue(String.class),
                            false);
                    song.setLocations(dsp.child("locations").getValue(t));
                    song.setUserNames(dsp.child("userNames").getValue(n));
                    song.setDate(OffsetDateTime.parse(dsp.child("date").getValue(String.class)));

                    Log.println(Log.ERROR, "extractFirebase", "Song is:" + song.toString());

                    // update current song object
                    c.callback(song);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DATBASEERROR", "Failed to read value.", error.toException());
            }
        });
    }
}
