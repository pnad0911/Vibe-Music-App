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
    private Date date;

    private Double previousloc_lat;
    private Double previousloc_long;
    private Date previousDate;

    private boolean isFavorite;
    private boolean isDisliked;


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

    public void setLocation(Double loc_lat, Double loc_long){
        this.loc_lat = loc_lat;
        this.loc_long = loc_long;
    }

    public void setDate(Date date){this.date = date; }

    public void setPreviousDate(Date date){this.previousDate = date; }

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

    public Double getLoc_lat(){ return this.loc_lat; }

    public Double getPreviousloc_lat(){return this.previousloc_lat;}

    public Double getPreviousloc_long(){return this.previousloc_long;}

    public Double getLoc_long(){ return this.loc_long; }

    public Date getDate(){ return  this.date; }

    public Date getPreviousDate(){return this.previousDate;}

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

}

