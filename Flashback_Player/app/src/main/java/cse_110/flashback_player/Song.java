package cse_110.flashback_player;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.location.Location;
        import android.util.Log;
        import android.util.Pair;
        import android.widget.Toast;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;
        import com.google.gson.Gson;

        import java.time.OffsetDateTime;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Patrick and Yutong on 2/7/2018.
 * Added new constructor (Mp3 file name)-------- Duy
 */

public class Song{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef = database.getReference();

    /* 1 -> favorited, 0 -> neutral, -1 -> disliked */
    private int like = 0;

    private String title;
    private int id;
    private String artist;
    private String album;

    // song history information
//    private Location previousLocation = null;
//    private Location currentLocation;

    private HashMap<String, String> locations = new HashMap<>();
    private Map<String, String> userNames = new HashMap<>();
    private String date;

//    private OffsetDateTime previousDate = null;
//    private OffsetDateTime currentDate;
//
//    private final double fiveam = 300; // times are in minutes
//    private final double elevenam = 660;
//    private final double fivepm = 1020;
//    private final double locRange = 1000; // feet
//    private final double latToFeet = 365228;
//    private final double longToFeet = 305775;

    public Song(){

    }

    // ----- CONSTRUCTORS ------------------------------------------
    public Song(String title, int id, String artist, String album){

        setTitle(title);
        setID(id);
        setArtist(artist);
        setAlbum(album);

        // try to get song from firebase
//        Query queryRef = databaseRef.orderByChild("title").equalTo(title);
//        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                if (snapshot == null || snapshot.getValue() == null){
//                    Log.println(Log.INFO, "info", "No such song as" + getTitle());
//                }
//                else{
//                    setPreviousLocation();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Faile to read value
//                Log.w("TAG1", "Failed to read value.", error.toException());
//            }
//        });
    }

    public Song(String title, int id, String artist, String album, Friend user){
        this.title = title;
        this.id = id;
        this.artist = artist;
        this.album = album;
        this.date = user.getTime();
        addLocation(user.getLocation());
        addUser(user.getID());
        this.update();
    }

    public Song(String title, String artist, String album){
        setTitle(title);
        setID(0);
        setArtist(artist);
        setAlbum(album);
    }


    public Song(Song song){
        setTitle(song.getTitle());
        setID(song.getID());
        setArtist(song.getArtist());
        setAlbum(song.getAlbum());
        addUser(song.getUser());
    }
    // --- END CONSTRUCTORS --------------------------------------

    //----- SETTERS -------------------------------------------
    private void setTitle(String title){
        this.title = title;
//        this.update();
    }

    private void setID(int id){
        this.id = id;
//        this.update();
    }

    private void setArtist(String artist) {
        this.artist = artist;
//        this.update();
    }

    private void setAlbum(String album){
        this.album = album;
//        this.update();
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


    public void addUser(String uid){
        userNames.put(uid,uid);
        //this.update();
    }

    public void addLocation(Location location){
        locations.put(Integer.toString((int) location.getLatitude()), Integer.toString((int) location.getLongitude()));
//        this.update();
    }

    public void setDate(Object time){
        this.date = time.toString();
//        this.update();
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

    public int getID(){ return id; }

    public String getArtist(){ return artist; }

    public String getAlbum(){
        return this.album;
    }

    public String getUser(){ return this.userNames.get(0); }

    public HashMap<String, String> getLocations(){ return locations; }

    public int getSongStatus (Context context) { return getPreviousLike(context); }

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

    // END GETTER -------------------------------------------------

    /* Method called as long as this object is modified. */
    public void update(){
        Log.println(Log.INFO, "Database", "Updating song " + this.title + " in Firebase.");
        DatabaseReference songDataRef = databaseRef.child("SONGS");
        songDataRef.child(this.title).setValue(this);
        songDataRef.child(this.title).child("userNames").setValue(userNames);
    }

    @Override
    public String toString(){
        return this.title;
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


