package cse_110.flashback_player;

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

    private Double loc_lat;
    private Double loc_long;

    private boolean isFavorite;
    private boolean isDisliked;

    private Double previousloc_lat;
    private Double previousloc_long;
    private OffsetDateTime previousDate;
    private OffsetDateTime currentDate;
    private final double fiveam = 300; // times are in minutes
    private final double elevenam = 660;
    private final double fivepm = 1020;
    private final double locRange = 1000; // feet

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


    public void setPreviousDate(OffsetDateTime date)
    {
        previousDate = date;
    }

    public void setCurrentDate(OffsetDateTime date){
        currentDate = date;
    }


    public void setPreviousLocation(Double loc_lat, Double loc_long){
        this.previousloc_lat = loc_lat;
        this.previousloc_long = loc_long;
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


    public OffsetDateTime getPreviousDate(){return this.previousDate; }

    public OffsetDateTime getCurrentDate(){return this.currentDate; }

    public Double getPreviousloc_lat(){return this.previousloc_lat;}

    public Double getPreviousloc_long(){return this.previousloc_long;}

    public Double getLoc_long(){ return this.loc_long; }

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
        double distance = Math.sqrt(Math.pow(loc_lat - previousloc_lat, 2) +
                Math.pow(loc_long - previousloc_long, 2));
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

}

