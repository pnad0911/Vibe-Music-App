package cse_110.flashback_player;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.location.Location;

        import com.google.gson.Gson;

        import java.time.OffsetDateTime;
        import java.time.ZoneOffset;
        import java.util.Date;

        import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Patrick and Yutong on 2/7/2018.
 * Added new constructor (Mp3 file name)-------- Duy
 */

public class Song {

    /* 1 -> favorited, 0 -> neutral, -1 -> disliked */
       /* 1 -> favorited, 0 -> neutral, -1 -> disliked */
    private int like;

    public void like() { like = 1; }
    public void dislike() { like = -1; }
    public void neutral() { like = 0; }
    public int getSongStatus() { return like;}


    private String title;
    private int id;
    private String artist;
    private String album;

    private OffsetDateTime timestamp;
    private Location previousLocation = null;
    private Location currentLocation;

    private Boolean isLiked;

    private OffsetDateTime previousDate = null;
    private OffsetDateTime currentDate = null;

    private final double fiveam = 300; // times are in minutes
    private final double elevenam = 660;
    private final double fivepm = 1020;
    private final double locRange = 1000; // feet
    private final double latToFeet = 365228;
    private final double longToFeet = 305775;

    public Song(String title, int id, String artist, String album){
        setTitle(title);
        setID(id);
        setArtist(artist);
        setAlbum(album);
    }

    public Song(String title, int id, String artist, String album, Location loc, OffsetDateTime time){
        setTitle(title);
        setID(id);
        setArtist(artist);
        setAlbum(album);
        this.previousDate = time;
        this.previousLocation = loc;
    }

    public Song(String title, String artist, String album){
        setTitle(title);
        setID(0);
        setArtist(artist);
        setAlbum(album);
    }

    public Song(Song song){
        setTitle(new String(song.getTitle()));
        setID(song.getID());
        setArtist(new String(song.getArtist()));
        setAlbum(new String(song.getAlbum()));
    }

    private void setTitle(String title){
        this.title = title;
    }

    private void setID(int id){
        this.id = id;
    }

    private void setArtist(String artist){
        this.artist = artist;
    }

    private void setAlbum(String album){
        this.album = album;
    }

    public void setPreviousDate() {
        timestamp = OffsetDateTime.now().minusHours(8);
        this.previousDate = timestamp;
    }

    public void setPreviousDate(Context context) {
        SharedPreferences sharedTime = context.getSharedPreferences("time", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedTime.edit();
        Gson gson = new Gson();
        String json = gson.toJson(OffsetDateTime.now().minusHours(8));
        editor.putString(getTitle(),json);
        editor.commit();
        setPreviousDateShared(OffsetDateTime.now().minusHours(8));
    }

    public void setPreviousDateShared(OffsetDateTime time) {
        this.previousDate = time;
    }

    public void setPreviousLocationShared(Location location) {
        this.previousLocation = location;
    }
    /**
     * for testing, delete later
     * @param time
     */
    public void setPreviousDate(OffsetDateTime time) {
        this.previousDate = time;
    }
    public String getTitle(){
        return title;
    }

    public int getID(){
        return id;
    }

    public String getArtist(){
        return artist;
    }

    public String getAlbum(){
        return this.album;
    }

    public Location getPreviousLocation(Context context){
        SharedPreferences sharedLocation = context.getSharedPreferences("location", MODE_PRIVATE);
        Gson gson2 = new Gson();
        String json2 = sharedLocation.getString(getTitle(), "");
        Location location = gson2.fromJson(json2, Location.class);
        setPreviousLocationShared(location);
        return this.previousLocation;
    }

    public Location getCurrentLocation(){ return this.currentLocation; }

    public OffsetDateTime getPreviousDate(Context context){
        try {
            SharedPreferences sharedTime = context.getSharedPreferences("time", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedTime.getString(getTitle(), "");
            OffsetDateTime time = gson.fromJson(json, OffsetDateTime.class);
            this.previousDate = time;
        } catch (Exception e) {
            this.previousDate = null;
        }
        return this.previousDate;
    }

    public OffsetDateTime getCurrentDate(){return this.currentDate; }

    /* true -> favorited, null -> neutral, false -> disliked */
    public void setLikedStatus(Boolean newStatus) {
        isLiked = newStatus;
    }

    public void setPreviousLocation(Location loc, Context context){
        SharedPreferences sharedLocation = context.getSharedPreferences("location", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedLocation.edit();
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(loc);
        editor2.putString(getTitle(),json2);
        this.previousLocation = loc;
    }
    public void setPreviousLocation(Location loc){
        this.previousLocation = loc;
    }
    public void setPreviousTime(OffsetDateTime time){
        this.previousDate = time;
    }

    public void setCurrentLocation(Location loc,Context context){
        SharedPreferences sharedLocation = context.getSharedPreferences("location", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedLocation.edit();
        Gson gson2 = new Gson();
        String json2 = gson2.toJson(loc);
        editor2.putString(getTitle(),json2);
        editor2.commit();
        this.currentLocation = loc;
    }

    /**
     * Gets weighted score of song (max 300)
     * @return
     */
    public double getScore(Location userLocation, OffsetDateTime presentTime) {
        double locScore = getLocationScore(userLocation);
        double dateScore = getDateScore(presentTime);
        double timeScore = getTimeScore(presentTime);
        return locScore + dateScore + timeScore;
    }

    /**
     * helper for getScore
     * @return location score
     */
    public double getLocationScore(Location userLocation) {
        /*try {
            previousLocation.getLatitude();
            previousLocation.getLongitude();
            userLocation.getLatitude();
            userLocation.getLongitude();
        } catch (RuntimeException e) {
            System.out.println("failed to get location in getLocationScore");
            return 0;
        }*/
        if (userLocation == null) {
            return 0;
        }
        double prevFeetLat = previousLocation.getLatitude() * latToFeet;
        System.out.println(previousLocation.getLatitude());
        double prevFeetLong = previousLocation.getLongitude() * longToFeet;
        double currFeetLat = userLocation.getLatitude() * latToFeet;
        System.out.println(userLocation.getLatitude());
        double currFeetLong = userLocation.getLongitude() * longToFeet;
        double distance = Math.sqrt(Math.pow(currFeetLat - prevFeetLat, 2) +
                Math.pow(currFeetLong - prevFeetLong, 2));
        if (distance > locRange) {
            return 0;
        }
        return 100 - (distance / 10);
    }

    /**
     * helper for getScore
     * @return date score
     */
    public double getDateScore(OffsetDateTime presentTime) {

        if (presentTime == null) {
            return 0;
        }
        if (presentTime.getDayOfWeek().getValue() == previousDate.getDayOfWeek().getValue())  {
            return 100;
        }
        return 0;
    }

    /**
     * helper for getDateScore
     * @return time score
     */
    public int getTimeScore(OffsetDateTime presentTime) {
        if (presentTime == null || previousDate == null){
            return 0;
        }
        double currentTime = presentTime.getHour()*60 + presentTime.getMinute();
        double previousTime = previousDate.getHour()*60 + previousDate.getMinute();
        String currentTimeOfDay = getTimeofDay(currentTime);
        String previousTimeOfDay = getTimeofDay(previousTime);
        if (currentTimeOfDay.equals(previousTimeOfDay)) {
            return 100;
        }
        return 0;
    }

    /**
     * helper for getTimeScore
     * @param time
     * @return String time of day
     */
    public String getTimeofDay(double time) {
        if (time >= fiveam && time <= elevenam) {
            return "morning";
        } else if (time > elevenam && time <= fivepm) {
            return "afternoon";
        } else {
            return "evening";
        }
    }
}

