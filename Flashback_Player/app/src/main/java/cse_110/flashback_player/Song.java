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

    private Boolean like;

    public Boolean songCurrentlyLiked() { return like;}
    public void likeSong() { like = true; }
    public void dislikeSong() { like = false; }
    public void neutralSong() {like = null; };

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


    public Song(String title, int id, String artist, String album){
        setTitle(title);
        setID(id);
        setArtist(artist);
        setAlbum(album);
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


    public void setCurrentDate()
    {
        timestamp = OffsetDateTime.now().minusHours(8);
        this.currentDate = timestamp;
//        if(this.previousDate == null){
//            this.previousDate = timestamp;
//        }
//        else{
//            this.previousDate = this.currentDate;
//        }
//        this.currentDate = timestamp;
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
        SharedPreferences sharedTime = context.getSharedPreferences("time", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedTime.getString(getTitle(), "");
        OffsetDateTime time = gson.fromJson(json, OffsetDateTime.class);
        this.previousDate = time;
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
        editor2.commit();
        this.previousLocation = loc;
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

    public Boolean getLikedStatus() {
        return isLiked;
    }

    /**
     * Gets weighted score of song (max 300)
     * @return
     */
    public double getScore() {
        double locScore = getLocationScore();
        double dateScore = getDateScore();
        double timeScore = getTimeScore();
        return locScore + dateScore + timeScore;
    }

    /**
     * helper for getScore
     * @return location score
     */
    private double getLocationScore() {
        double distance = Math.sqrt(Math.pow(this.currentLocation.getLatitude() - this.previousLocation.getLatitude(), 2) +
                Math.pow(this.currentLocation.getLongitude() - this.previousLocation.getLongitude(), 2));
        if (distance > locRange) {
            return 0;
        }
        return 100 - (distance/10);
    }

    /**
     * helper for getScore
     * @return date score
     */
    private double getDateScore() {
        if (currentDate.getDayOfWeek().getValue() == previousDate.getDayOfWeek().getValue())  {
            return 100;
        }
        return 0;
    }

    /**
     * helper for getScore
     * @return time score
     */
    private double getTimeScore() {
        double currentTime = currentDate.getHour()*60 + currentDate.getMinute();
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
    private String getTimeofDay(double time) {
        if (time >= fiveam && time <= elevenam) {
            return "morning";
        } else if (time > elevenam && time <= fivepm) {
            return "afternoon";
        } else {
            return "evening";
        }
    }

//    private void updateTimeNLocation(Song song, Context context) {
//        SharedPreferences sharedTime = context.getSharedPreferences("time", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedTime.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(song.getPreviousDate());
//        editor.putString(song.getTitle(),json);
//        editor.commit();
//        SharedPreferences sharedLocation = context.getSharedPreferences("location", MODE_PRIVATE);
//        SharedPreferences.Editor editor2 = sharedLocation.edit();
//        Gson gson2 = new Gson();
//        String json2 = gson2.toJson(song.getPreviousLocation());
//        editor2.putString(song.getTitle(),json2);
//        editor2.commit();
//    }
//    private void getTimeNLocation(Song song, Context context) {
//        SharedPreferences sharedTime = context.getSharedPreferences("time", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedTime.getString(song.getTitle(), "");
//        OffsetDateTime time = gson.fromJson(json, OffsetDateTime.class);
//        song.setPreviousDateShared(time);
//        SharedPreferences sharedLocation = context.getSharedPreferences("location", MODE_PRIVATE);
//        Gson gson2 = new Gson();
//        String json2 = sharedLocation.getString(song.getTitle(), "");
//        Location location = gson2.fromJson(json2, Location.class);
//        song.setPreviousLocationShared(location);
//    }

}

