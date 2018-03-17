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

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
//import static cse_110.flashback_player.logIn.user;
import static java.sql.DriverManager.println;

/**
 * Created by Patrick and Yutong on 2/7/2018.
 * Added new constructor (Mp3 file name)-------- Duy
 */

public class Song implements SongSubject{
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference databaseRef = database.getReference();

//    private Boolean downloaded = false;

//    public int getId() {return id;}


    private final double locRange = 1000; //feet
    private final double latToFeet = 365228;
    private final double longToFeet = 305775;

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


    /* -------------------------------  CONSTRUCTORS  ------------------------------------------*/

    public Song(){ }

    /* If created from local file, the song will not have user, location or date info */
    public Song (String title, String artist, String album, String url, boolean local){
        this.title = title;

        if (local){
            this.localPath = url;
            Database.loadSong(this);
            try{ Thread.sleep(1000); } catch (Exception e){ e.printStackTrace();} //wait for data

        }
        else {
            this.songUrl = url;
        }

        this.artist = artist;
        this.album = album;
        this.databaseKey = title+artist;
        Log.println(Log.ERROR, "SongFirebase: ", databaseKey);
        Log.println(Log.ERROR, "SongCONST: ", Integer.toString(logIn.user.getFriendlist().size()));

    }

//    /* Local song creation */
//    public Song(String title, String artist, String album, String path, Boolean local){
//        this.title = title;
//        this.localPath = path;
//        this.artist = artist;
//        this.album = album;
//    }

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

    public void setDatabaseKey(String key) { this.databaseKey = key;}

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

    public void addUser(String name){
        userNames.add(name);
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
    public String getArtist(){ if (artist == null) {return "null";} else {return artist;} }
    public String getAlbum(){  if (album == null) {return "null";} else {return album;} }
    public String getDate(){ return this.date; }
    public String getDatabaseKey(){ return this.title+this.artist;}
    public ArrayList<HashMap<String,String>> getLocations(){ return this.locations;}
    public ArrayList<String> getUserNames(){ return this.userNames; }

    /* ----------------------------- Helper Getters ------------------------------------------- */

    public Pair<String,String> previousLocation () {
        HashMap<String,String> hm =  locations.get(locations.size()-1);
        return new Pair<String,String>(hm.get("Latitude"),hm.get("Longitude"));
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
        try {
            for (String a : userNames) {
                if (friends.contains(a)) {
                    returnVal = a;
                }
            }
            if (userNames.contains(me)) {
                returnVal = "you";
            } else {
                if (returnVal == "") {
                    returnVal = Integer.toString(userNames.get(userNames.size() - 1).hashCode());
                }
            }
        } catch (Exception e){
            return "";
        }
        return returnVal;
    }

    /* ----------------------------- SharePreference Getters ---------------------------------- */

    public int getSongStatus () { return getPreviousLike(); }

    public Boolean getDownloadStatus() {
        try {
            SharedPreferences sharedTime = LibraryActivity.getContextOfApplication().getSharedPreferences("download", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedTime.getString(getTitle(), "");
            Boolean down = gson.fromJson(json, Boolean.class);
            return down;
        } catch (Exception e) {
            return false;
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

   

    /**
     * Implementing DatabaseListener
     */
    public void update(Song song){

        if (song.getLocations()!=null && song.getLocations().size() >= 100){
            locations = new ArrayList<>(song.getLocations().subList(song.getLocations().size()-101, song.getLocations().size()-1));
        } else { locations = song.getLocations(); }

        if (song.getUserNames()!=null && song.getUserNames().size() >= 100){
            userNames = new ArrayList<>(song.getUserNames().subList(song.getUserNames().size()-101, song.getUserNames().size()-1));
        } else { userNames = song.getUserNames(); }

        if (song.getSongUrl() == null) {
            songUrl  = "";
        } else { this.setSongUrl(song.getSongUrl()); }

        date = song.getDate();
        title = song.getTitle();
        artist = song.getArtist();
        album = song.getAlbum();
        databaseKey = song.getDatabaseKey();

        Log.println(Log.ERROR, "FROM DATABASE", "New date: " + date);
        Log.println(Log.ERROR, "CONSTR", "Songurl is: "+songUrl);

        for (SongObserver ob : observers){
            ob.update();
        }
    }

    /* -------------------------  Weighting System  ------------------------------------------- */
    /**
     * Score for each song, max score 303
     * @param userLocation
     * @param presentTime
     * @return song score
     */
    public double getScore(Location userLocation, OffsetDateTime presentTime) {
        double locScore = getLocationScore(userLocation);
        double weekScore = getWeekScore(presentTime);
        double friendScore = getFriendScore();
        return locScore + weekScore + friendScore;
    }

    /**
     * Songs not downloaded yet are pushed to the bottom of the queue
     * @param userLocation
     * @param presentTime
     * @return new score
     */
    public double getScore2(Location userLocation, OffsetDateTime presentTime) {
        if (!getDownloadStatus()) {
            return -5;
        }
        double locScore = getLocationScore(userLocation);
        double weekScore = getWeekScore(presentTime);
        double friendScore = getFriendScore();
        return locScore + weekScore + friendScore;
    }

    /**
     * helper for getScore
     * @return location score
     */
    public double getLocationScore(Location userLocation) {

        if (userLocation == null) {
            return 0;
        }

        double prevFeetLat = Double.parseDouble(previousLocation().first) * latToFeet;
        double prevFeetLong = Double.parseDouble(previousLocation().second) * longToFeet;
        double currFeetLat = userLocation.getLatitude() * latToFeet;
        double currFeetLong = userLocation.getLongitude() * longToFeet;
        double distance = Math.sqrt(Math.pow(currFeetLat - prevFeetLat, 2) +
                Math.pow(currFeetLong - prevFeetLong, 2));
        // check if song was played within 1000 ft
        if (distance > locRange) {
            return 0;
        }
        return 102;
    }

    /**
     * helper for getScore
     * @param presentTime
     * @return week score
     */
    public double getWeekScore(OffsetDateTime presentTime) {
        OffsetDateTime playedTime = OffsetDateTime.parse(getDate());
        if (playedTime == null) {
            return 0;
        }
        OffsetDateTime lastWeekBegin = presentTime.minusDays(7);
        // Monday (value 1) to Sunday (value 7) is a week
        while (lastWeekBegin.getDayOfWeek().getValue() > 1) {
            lastWeekBegin = lastWeekBegin.minusDays(1);
        }

        for (int i = 0; i < 7; i++) {
            if (lastWeekBegin.getYear() == playedTime.getYear() &&
                    lastWeekBegin.getDayOfYear() == playedTime.getDayOfYear()) {
                return 101;
            }
            lastWeekBegin = lastWeekBegin.plusDays(1);
        }
        return 0;
    }

    /**
     * helper for getScore
     * @return friend score
     */
    public double getFriendScore() {
        if (isPlayedByFriend()) {
            return 100;
        }
        return 0;
    }

    /**
     * helper for getFriendScore
     * @return true if song was played by friend, false otherwise
     */
    public boolean isPlayedByFriend() {

        ArrayList<String> usersPlayedSong = this.getUserNames();
        ArrayList<String> friends = logIn.user.getFriendlist();

        if (friends.size() == 0 || usersPlayedSong.size() == 0) {
            return false;
        }

        for (String username : usersPlayedSong) {
            for (String friend : friends) {
                if (username.equals(friend)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * method for testing
     */
    private Location loc;
    public void setLocation(Location loc) {
        this.loc = loc;
    }
    public void setUser(String string) {
        this.userNames.add(new String(string));
    }

    @Override
    public String toString(){ return this.title; }

    public int getStatus(){return like;}

}

