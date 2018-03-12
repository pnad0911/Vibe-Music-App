package cse_110.flashback_player;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Patrick and Yutong on 2/7/2018.
 * Added new constructor (Mp3 file name)-------- Duy
 */

public class Song implements SongSubject{
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference databaseRef = database.getReference();

    private boolean downloaded = false;
    public void setDownloaded() {
        downloaded = true;
    }
    public boolean getDownloadStatus() {
        return downloaded;
    }
    public int getId() {return id;}

    private GenericTypeIndicator<ArrayList<HashMap<String,String>>> t = new GenericTypeIndicator<ArrayList<HashMap<String,String>>>() {};

    /* 1 -> favorited, 0 -> neutral, -1 -> disliked */
    private int like = 0;

    private String title;
    private String songUrl;
    private String artist;
    private String album;
    private String databaseKey;

    private int id;

    private ArrayList<SongObserver> observers = new ArrayList<>();
    public void reg(SongObserver ob){
        observers.add(ob);
    }

    // song history information
//    private Location previousLocation = null;
//    private Location currentLocation;
    private String date;
    private ArrayList<HashMap<String,String>> locations = new ArrayList<>();
    private ArrayList<HashMap<String,String>> userNames = new ArrayList<>();


//    private OffsetDateTime previousDate = null;
//    private OffsetDateTime currentDate;
//
//    private final double fiveam = 300; // times are in minutes
//    private final double elevenam = 660;
//    private final double fivepm = 1020;
//    private final double locRange = 1000; // feet
//    private final double latToFeet = 365228;
//    private final double longToFeet = 305775;



    // ----- CONSTRUCTORS ------------------------------------------

    public Song(){ }

    /* If created from local file, the song will not have user, location or date info */
    public Song (String title, String artist, String url, String album){
        this.title = title;
        this.songUrl = url;
        this.artist = artist;
        this.album = album;
        this.databaseKey = title+artist;
        loadFromDatabase(new dataBaseCallback() {
            @Override
            public void callback(ArrayList<HashMap<String,String>> u, ArrayList<HashMap<String,String>> l, String d, String url) {
                locations = l;
                userNames = u;
                date = d;
                songUrl = url;
            }
        });
        try{ Thread.sleep(1000); } catch (Exception e){ e.printStackTrace();} //wait for data
        Log.println(Log.ERROR, "FROM DATABASE", "New date: " + date);
    }

//    public Song(String title, String songUrl, String artist, String album){
//        this.title = title;
//        this.songUrl = songUrl;
//        this.artist = artist;
//        this.album = album;
//        this.databaseKey = this.title+this.artist;
//    }

    /* Local song creation */
    public Song(String title, int id, String artist, String album){
        this.title = title;
        this.id = id;
        this.artist = artist;
        this.album = album;
    }

    private void setSong (Song song){
        Log.println(Log.ERROR, "SETSONG", song.getSongUrl());
        this.title = song.title;
        this.songUrl = song.songUrl;
        this.artist = song.artist;
        this.album = song.album;
        this.date = song.date;
        this.locations = song.locations;
        this.userNames = song.userNames;
        this.databaseKey = song.title+song.artist;
    }
    // --- END CONSTRUCTORS --------------------------------------

    //----- SETTERS -------------------------------------------
    public void setTitle(String title){
        this.title = title;
    }
    public void setSongUrl(String songUrl){
        this.songUrl = songUrl;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public void setAlbum(String album){
        this.album = album;
    }
    public void setDatabaseKey(String artist, String title) { this.databaseKey = title+artist;}
    public void setDate(Object time){
        this.date = time.toString();
    }
    public void setLocations(ArrayList<HashMap<String,String>> l){
        this.locations = l;
    }
    public void setUserNames(ArrayList<HashMap<String,String>> u) {
        this.userNames = u;
    }

    /* Favorite status is stored locally.*/
    public void setPreviousLike(int like, Context context){
        SharedPreferences sharedLocation = context.getSharedPreferences("like", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedLocation.edit();
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(like);
        editor2.putString(getTitle(),json2);
        editor2.commit();
        this.like = like;
    }

    public void addID (int id){ this.id = id; }

    public void addUser(String first, String last){
        HashMap<String,String> hm = new HashMap<>();
        hm.put(first+last, first+last);
        userNames.add(hm);
    }

    public void addLocation(Location location){
        HashMap<String,String> hm = new HashMap<>();
        hm.put(Integer.toString((int) location.getLatitude()), Integer.toString((int) location.getLongitude()));
        locations.add(hm);
    }

    public void like(Context context) { like = 1; setPreviousLike(like, context);}
    public void dislike(Context context) { like = -1; setPreviousLike(like, context);}
    public void neutral(Context context) { like = 0; setPreviousLike(like, context); }

    //    public void setPreviousDate(Context context) {
//        SharedPreferences sharedTime = context.getSharedPreferences("time", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedTime.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(OffsetDateTime.now().minusHours(8));
//        editor.putString(getTitle(),json);
//        editor.commit();
//        System.out.println(sharedTime.contains(getTitle()));
//        this.previousDate = OffsetDateTime.now().minusHours(8);
//        this.update();
//    }

//    public void setPreviousLocationShared(Location location) {
////        this.previousLocation = location;
//        this.update();
//    }


    // END SETTER -----------------------------------------------

    //    public void setPreviousDate(OffsetDateTime time) {
//        this.previousDate = time;
//    }
    public String getTitle(){
        return title;
    }

    public String getSongUrl(){ return songUrl; }
    public String getArtist(){ return artist; }
    public String getAlbum(){
        return this.album;
    }
    public ArrayList<HashMap<String,String>> getLocations(){ return this.locations;}
    public ArrayList<HashMap<String,String>> getUserNames(){ return this.userNames; }

    public ArrayList<Pair<String,String>> getAllLocations(){
        ArrayList<Pair<String,String>> toReturn = new ArrayList<>();
        for (HashMap<String,String> hm : this.locations){
            ArrayList<String> latArr = new ArrayList<>(hm.keySet());
            ArrayList<String> longArr = new ArrayList<>(hm.values());
            toReturn.add(new Pair<>(latArr.get(0), longArr.get(0)));
        }
        return toReturn;
    }

    public int getSongStatus (Context context) { return getPreviousLike(context); }

    public Pair<String,String> getPreviousLocation () {
        HashMap<String,String> loc = locations.get(locations.size()-1);
        ArrayList<String> latArr = new ArrayList<>(loc.keySet());
        ArrayList<String> longArr = new ArrayList<>(loc.values());
        return new Pair<>(latArr.get(0),longArr.get(0));
    }

    public int getPreviousLike(Context context){
        try {
            SharedPreferences sharedTime = context.getSharedPreferences("like", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedTime.getString(getTitle(), "");
            Integer liked = gson.fromJson(json, Integer.class);
            this.like = liked;
        } catch (Exception e) {
            this.like = 0;
        }
        return this.like;
    }

    public String getDate(){
        return this.date;
    }

    public String getDatabaseKey(){ return this.title+this.artist;}

    // END GETTER -------------------------------------------------

    /* Method called as long as this object is modified. */
    public void updateDatabase(){
        Log.println(Log.ERROR, "Database", "Updating song " + this.title + " in Firebase.");
        DatabaseReference songDataRef = databaseRef.child("SONGS");
        songDataRef.child(this.databaseKey).setValue(this);
//        songDataRef.child(this.title+this.artist).child("userNames").setValue(userNames);
        for (SongObserver ob : observers){
            ob.update();
        }
    }

    @Override
    public String toString(){
        return this.title;
    }

    /* Load song from database*/
    private void loadFromDatabase(final dataBaseCallback c){
        Log.println(Log.ERROR, "GETINSTANCE", "WEEEEEEE");
        DatabaseReference songRef = databaseRef.child("SONGS").getRef();
        Query queryRef = songRef.orderByChild("databaseKey").equalTo(databaseKey);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot == null || snapshot.getValue() == null) {
                    Log.println(Log.ERROR, "GETINSTANCE", "No such song as "+databaseKey);

                } else {
                    Log.println(Log.ERROR, "GETINSTANCE", "Found song " + databaseKey);

                    // update current song object
                    c.callback(snapshot.child(databaseKey).child("userNames").getValue(t),
                            snapshot.child(databaseKey).child("locations").getValue(t),
                            snapshot.child(databaseKey).child("date").getValue(String.class),
                            snapshot.child(databaseKey).child("songUrl").getValue(String.class));

                    // update song observers (refresh Song List)
                    for (SongObserver ob : observers) { ob.update();}
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Fail to read value
                Log.w("TAG1", "Failed to read value.", error.toException());
            }
        });
    }

    /* To update database */
    interface dataBaseCallback {
        public abstract void callback(ArrayList<HashMap<String,String>> user,
                                      ArrayList<HashMap<String,String>> locations,
                                      String date,
                                      String url);
    }

//    public Location getCurrentLocation(){ return this.currentLocation; }

//    public OffsetDateTime getPreviousDate(Context context){
//        try {
//            SharedPreferences sharedTime = context.getSharedPreferences("time", MODE_PRIVATE);
//            Gson gson = new Gson();
//            String json = sharedTime.getString(getTitle(), "");
//            OffsetDateTime time = gson.fromJson(json, OffsetDateTime.class);
//            this.previousDate = OffsetDateTime.now().minusHours(8);
//        } catch (Exception e) {
//            this.previousDate = null;
//        }
//        return this.previousDate;
//    }
//

    //    public Location getPreviousLocation(Context context){
//        SharedPreferences sharedLocation = context.getSharedPreferences("location", MODE_PRIVATE);
//        Gson gson2 = new Gson();
//        String json2 = sharedLocation.getString(getTitle(), "");
//        Location location = gson2.fromJson(json2, Location.class);
//        setPreviousLocationShared(location);
//        return location;
//    }

//    public OffsetDateTime getCurrentDate(){ return this.currentDate; }

//    public void setPreviousLocation(Location loc, Context context){
//        SharedPreferences sharedLocation = context.getSharedPreferences("location", MODE_PRIVATE);
//        SharedPreferences.Editor editor2 = sharedLocation.edit();
//        Gson gson2 = new Gson();
//        String json2 = gson2.toJson(loc);
//        editor2.putString(getTitle(),json2);
//        editor2.commit();
////        this.previousLocation = loc;
//    }
//

//    public void setPreviousLocation(Location loc){
////        this.previousLocation = loc;
//    }
//    public void setPreviousTime(OffsetDateTime time){
//        this.previousDate = time;
//    }
//
//    public void setCurrentLocation(Location loc,Context context){
//        SharedPreferences sharedLocation = context.getSharedPreferences("location", MODE_PRIVATE);
//        SharedPreferences.Editor editor2 = sharedLocation.edit();
//        Gson gson2 = new Gson();
//        String json2 = gson2.toJson(loc);
//        editor2.putString(getTitle(),json2);
//        editor2.commit();
//        this.currentLocation = loc;
//    }

    /**
     * Gets weighted score of song (max 300)
     * @return
     */
    public double getScore(Location userLocation, OffsetDateTime presentTime) {
//        double locScore = getLocationScore(userLocation);
//        double dateScore = getDateScore(presentTime);
//        double timeScore = getTimeScore(presentTime);
//        return locScore + dateScore + timeScore;
        return 1;
    }
//
//    /**
//     * helper for getScore
//     * @return location score
//     */
//    public double getLocationScore(Location userLocation) {
//        /*try {
//            previousLocation.getLatitude();
//            previousLocation.getLongitude();
//            userLocation.getLatitude();
//            userLocation.getLongitude();
//        } catch (RuntimeException e) {
//            System.out.println("failed to get location in getLocationScore");
//            return 0;
//        }*/
////        if (userLocation == null) {
////            return 0;
////        }
////        double prevFeetLat = previousLocation.getLatitude() * latToFeet;
////        System.out.println(previousLocation.getLatitude());
////        double prevFeetLong = previousLocation.getLongitude() * longToFeet;
////        double currFeetLat = userLocation.getLatitude() * latToFeet;
////        System.out.println(userLocation.getLatitude());
////        double currFeetLong = userLocation.getLongitude() * longToFeet;
////        double distance = Math.sqrt(Math.pow(currFeetLat - prevFeetLat, 2) +
////                Math.pow(currFeetLong - prevFeetLong, 2));
////        if (distance > locRange) {
////            return 0;
////        }
//        return 100; // - (distance / 10);
//    }
//
//    /**
//     * helper for getScore
//     * @return date score
//     */
//    public double getDateScore(OffsetDateTime presentTime) {
//
////        if (presentTime == null) {
////            return 0;
////        }
////        if (presentTime.getDayOfWeek().getValue() == previousDate.getDayOfWeek().getValue())  {
////            return 100;
////        }
//        return 0;
//    }
//
//    /**
//     * helper for getDateScore
//     * @return time score
//     */
//    public int getTimeScore(OffsetDateTime presentTime) {
//        if (presentTime == null || previousDate == null){
//            return 0;
//        }
//        double currentTime = presentTime.getHour()*60 + presentTime.getMinute();
//        double previousTime = previousDate.getHour()*60 + previousDate.getMinute();
//        String currentTimeOfDay = getTimeofDay(currentTime);
//        String previousTimeOfDay = getTimeofDay(previousTime);
//        if (currentTimeOfDay.equals(previousTimeOfDay)) {
//            return 100;
//        }
//        return 0;
//    }
//
//    /**
//     * helper for getTimeScore
//     * @param time
//     * @return String time of day
//     */
//    public String getTimeofDay(double time) {
//        if (time >= fiveam && time <= elevenam) {
//            return "morning";
//        } else if (time > elevenam && time <= fivepm) {
//            return "afternoon";
//        } else {
//            return "evening";
//        }
//    }

}

