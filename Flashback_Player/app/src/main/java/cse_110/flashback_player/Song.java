package cse_110.flashback_player;

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
    private int day;
    private int year;
    private String month;
    private int hour;
    private int minute;
    private Double previousloc_lat;
    private Double previousloc_long;
    private Date previousDate;

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

    public void setPreviousDate(int year, String month, int day, int hour, int minutes)
    {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minutes;
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


    public Double getPreviousloc_lat(){return this.previousloc_lat;}

    public Double getPreviousloc_long(){return this.previousloc_long;}

    public int getPreviousYear(){return this.year;}

    public String getPreviousMonth(){return this.month;}

    public int getPreviousDay(){return this.day;}

    public int getPreviousMinutes(){return this.minute;}

    public int getPreviousHour(){return this.hour;}
}

