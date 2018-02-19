package cse_110.flashback_player;

        import android.location.Location;

        import java.time.OffsetDateTime;
        import java.time.ZoneOffset;
        import java.util.Date;

/**
 * Created by Patrick and Yutong on 2/7/2018.
 * Added new constructor (Mp3 file name)-------- Duy
 */

public class Song {

    private String title;
    private int id;
    private String artist;
    private String album;

    private OffsetDateTime timestamp;
    private Location previousLocation;
    private Location currentLocation;

    private boolean isFavorite;
    private boolean isDisliked;

    private OffsetDateTime previousDate;
    private OffsetDateTime currentDate;
    private final double fiveam = 300; // times are in minutes
    private final double elevenam = 660;
    private final double fivepm = 1020;
    private final double locRange = 1000; // feet
    private final double latToFeet = 365228;
    private final double longToFeet = 305775;

    private String nameofMP3file;
    private final String RAWPATH = "app/src/main/res/raw/";

    public Song(String nameofMP3file) {
        this.nameofMP3file = nameofMP3file;
    }

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

        if(this.previousDate == null){
            this.previousDate = timestamp;
        }
        else{
            this.previousDate = this.currentDate;
        }
        this.currentDate = timestamp;
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

    public Location getPreviousLocation(){ return this.previousLocation; }

    public Location getCurrentLocation(){ return this.currentLocation; }


    public OffsetDateTime getPreviousDate(){return this.previousDate; }

    public OffsetDateTime getCurrentDate(){return this.currentDate; }


    public void toggleFavorite(){
        isFavorite = !isFavorite;
    }

    public void toggleDisliked(){
        isDisliked = !isDisliked;
    }

    public boolean getIsFavorite(){
        return isFavorite;
    }

    public boolean getIsDisliked(){
        return isDisliked;
    }

    public void setPreviousLocation(Location loc){
        this.previousLocation = loc;
    }

    public void setCurrentLocation(Location loc){
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

