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

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Patrick and Yutong on 2/7/2018.
 * Added new constructor (Mp3 file name)-------- Duy
 */

public class Song implements SongSubject{
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference databaseRef = database.getReference();

//    private Boolean downloaded = false;

//    public int getId() {return id;}

    private GenericTypeIndicator<ArrayList<HashMap<String,String>>> t = new GenericTypeIndicator<ArrayList<HashMap<String,String>>>() {};
    private GenericTypeIndicator<ArrayList<String>> n = new GenericTypeIndicator<ArrayList<String>>() {};

    /* 1 -> favorited, 0 -> neutral, -1 -> disliked */
    private int like = 0;

    private String title;
    private String songUrl = "";
    private String artist;
    private String album;
    private String databaseKey;

    public String localPath; //This can be set directly without calling setters

    private ArrayList<SongObserver> observers = new ArrayList<>();
    public void reg(SongObserver ob){
        observers.add(ob);
    }

    // song history information
//    private Location previousLocation = null;
//    private Location currentLocation;
    private String date;
    private ArrayList<HashMap<String,String>> locations = new ArrayList<>();
    private ArrayList<String> userNames = new ArrayList<>();


//    private OffsetDateTime previousDate = null;
//    private OffsetDateTime currentDate;
//
//    private final double fiveam = 300; // times are in minutes
//    private final double elevenam = 660;
//    private final double fivepm = 1020;
//    private final double locRange = 1000; // feet
//    private final double latToFeet = 365228;
//    private final double longToFeet = 305775;



    /* -------------------------------  CONSTRUCTORS  ------------------------------------------*/

    public Song(){ }

    /* If created from local file, the song will not have user, location or date info */
    public Song (String title, String artist, String album, String url, boolean local){
        this.title = title;
        if (local){
            this.localPath = url;
        }
        else {
            this.songUrl = url;
        }
        this.artist = artist;
        this.album = album;
        this.databaseKey = title+artist;
        loadFromDatabase(new dataBaseListener() {
            @Override
            public void callback(ArrayList<String> u, ArrayList<HashMap<String,String>> l, String d, String url) {

                if (l!=null && l.size() >= 100){ l = new ArrayList<>(l.subList(l.size()-101, l.size()-1)); }
                if (u!=null && u.size() >= 100){ u = new ArrayList<>(u.subList(u.size()-101, l.size()-1)); }

                locations = l;
                userNames = u;
                date = d;
                if (songUrl == null){songUrl = "";}
                songUrl = url;
//                Log.println(Log.ERROR, "FROM DATABASE", "New date: " + date);
            }
        });
        try{ Thread.sleep(1000); } catch (Exception e){ e.printStackTrace();} //wait for data
        Log.println(Log.ERROR, "FROM DATABASE", "New date: " + date);
        Log.println(Log.ERROR, "CONSTR", "Songurl is: "+songUrl);
    }

    /* Local song creation */
    public Song(String title, String artist, String album, String path, Boolean local){
        this.title = title;
        this.localPath = path;
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

    /* ----------------------------- Default Setters ------------------------------------------ */

    public void setTitle(String title){
        this.title = title;
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

    public void setUserNames(ArrayList<String> u) {
        this.userNames = u;
    }

    /* ------------------------  SharedPreference Setters  ------------------------------------ */

    public void setSongUrl(String songUrl){
        SharedPreferences sharedLocation = LibraryActivity.getContextOfApplication().getSharedPreferences("songUrl", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedLocation.edit();
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(songUrl);
        editor2.putString(getTitle(),json2);
        editor2.commit();
        this.songUrl = songUrl;

    }

    /* Favorite Status*/
    public void setPreviousLike(int like){
        SharedPreferences sharedLocation = LibraryActivity.getContextOfApplication().getSharedPreferences("like", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedLocation.edit();
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(like);
        editor2.putString(getTitle(),json2);
        editor2.commit();
        this.like = like;
    }

    public void setDownloaded() {
        SharedPreferences sharedTime = LibraryActivity.getContextOfApplication().getSharedPreferences("download", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedTime.edit();
        Gson gson = new Gson();
        String json = gson.toJson(true);
        editor.putString(getTitle(),json);
        editor.commit();
    }

    /* -----------------------------  Database Add  -------------------------------------------- */

    public void addUser(String first, String last){
        userNames.add(first+last);
    }

    public void addLocation(Location location){
        HashMap<String,String> hm = new HashMap<>();
        hm.put("Latitude", Float.toString((float) location.getLatitude()));
        hm.put("Longitude", Float.toString((float) location.getLongitude()));
        locations.add(hm);
    }

    public void like() { like = 1; setPreviousLike(like);}
    public void dislike() { like = -1; setPreviousLike(like);}
    public void neutral() { like = 0; setPreviousLike(like); }


    /* -----------------------------  Default Getters ----------------------------------------- */

    public String getTitle(){ return title; }
    public String getArtist(){ return artist; }
    public String getAlbum(){ return this.album;}
    public String getDate(){ return this.date; }
    public String getDatabaseKey(){ return this.title+this.artist;}
    public ArrayList<HashMap<String,String>> getLocations(){ return this.locations;}
    public ArrayList<String> getUserNames(){ return this.userNames; }

    /* ----------------------------- Helper Getters ------------------------------------------- */

    public Pair<String,String> previousLocation () {
        HashMap<String,String> hm =  locations.get(locations.size()-1);
        ArrayList<String> lat = new ArrayList<>(hm.keySet());
        ArrayList<String> lon = new ArrayList<>(hm.values());
        return new Pair<String,String>(lat.get(0),lon.get(0));
    }

    public ArrayList<Pair<String,String>> allLocations(){
        ArrayList<Pair<String,String>> returnV = new ArrayList<>();
        for (HashMap<String,String>hm : locations){
            returnV.add(new Pair<>(hm.get("Latitude"),hm.get("Longitude")));
        }
        return returnV;
    }

    public String getUser(ArrayList<String> friends, String me){
        String returnVal = "";
        for (String a : userNames){
            if (friends.contains(a)){
                returnVal = a;
            }
        }
        if (userNames.contains(me)){
            returnVal = me;
        }
        else{
            if (returnVal == "") {
                returnVal = Integer.toString(userNames.get(userNames.size() - 1).hashCode());
            }
        }
        return returnVal;
    }

    /* ----------------------------- SharePreference Getters ---------------------------------- */

    public int getSongStatus () { return getPreviousLike(); }

    public Boolean getDownloadStatus() {
        try {
            SharedPreferences sharedTime = LibraryActivity.getContextOfApplication().getSharedPreferences("time", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedTime.getString(getTitle(), "");
            Boolean down = gson.fromJson(json, Boolean.class);
            return down;
        } catch (Exception e) {
            return null;
        }
    }

    public int getPreviousLike(){
        try {
            SharedPreferences sharedTime = LibraryActivity.getContextOfApplication().getSharedPreferences("like", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedTime.getString(getTitle(), "");
            Integer liked = gson.fromJson(json, Integer.class);
            this.like = liked;
        } catch (Exception e) {
            this.like = 0;
        }
        return this.like;
    }

    public String getSongUrl(){
        try {
            SharedPreferences sharedTime = LibraryActivity.getContextOfApplication().getSharedPreferences("songUrl", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedTime.getString(getTitle(), "");
            String url = gson.fromJson(json, String.class);
            if (url == null){
                url = "";
            }
            this.songUrl = url;
        } catch (Exception e) {
            this.songUrl = "";
        }
        Log.println(Log.ERROR, "getSongURL", "Songurl is: "+songUrl);
        return this.songUrl;
    }

    /* ----------------------------  Database Methods  ---------------------------------------- */

    /** Method called as long as this object is modified.
     *  The display is responsible to call this method when location/date/user is set.
     */
    public void updateDatabase(){
        Log.println(Log.ERROR, "songURL", "Songurl is: "+songUrl);
        if (songUrl == null){songUrl = "";}
        Log.println(Log.ERROR, "Database", "Updating song " + this.title + " in Firebase.");
        DatabaseReference songDataRef = databaseRef.child("SONGS");

        songDataRef.child(this.databaseKey).child("title").setValue(this.title);
        songDataRef.child(this.databaseKey).child("artist").setValue(this.artist);
        songDataRef.child(this.databaseKey).child("databaseKey").setValue(this.databaseKey);
        songDataRef.child(this.databaseKey).child("album").setValue(this.album);
        songDataRef.child(this.databaseKey).child("songUrl").setValue(this.songUrl);

        songDataRef.child(this.databaseKey).child("userNames").setValue(this.userNames);
        songDataRef.child(this.databaseKey).child("locations").setValue(this.locations);
        songDataRef.child(this.databaseKey).child("date").setValue(this.date);

        for (SongObserver ob : observers){
            ob.update();
        }
    }

    /**
     * Load from Database.
     * Once called, will be monitoring the database constantly and will be updating the
     *          song object once there is a change in database.
     * @param c dataBaseListener, will be updated when the database changed
     * */
    private void loadFromDatabase(final dataBaseListener c){
        Log.println(Log.ERROR, "GETINSTANCE", "WEEEEEEE");
        DatabaseReference songRef = databaseRef.child("SONGS").getRef();
        Query queryRef = songRef.orderByChild("databaseKey").equalTo(databaseKey);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot == null || snapshot.getValue() == null) {
                    Log.println(Log.ERROR, "LoadFromDatabase", "No such song as "+databaseKey);

                } else {
                    Log.println(Log.ERROR, "LoadFromDatabase", "Found song " + databaseKey);

                    // update current song object
                    c.callback(snapshot.child(databaseKey).child("userNames").getValue(n),
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

    /**
     * Database listener
     */
    interface dataBaseListener {
        void callback(ArrayList<String> user, ArrayList<HashMap<String,String>> locations, String date, String url);
    }

    /* -------------------------  Weighting System  ------------------------------------------- */
    /**
     * Gets weighted score of song (max 300)
     * @return
     */
    public double getScore(Location userLocation, OffsetDateTime presentTime) { return 1; }

    @Override
    public String toString(){ return this.title; }

}

