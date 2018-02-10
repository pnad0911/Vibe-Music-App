package cse_110.flashback_player;

/**
<<<<<<< HEAD
 * Created by Patrick on 2/7/2018.
=======
 * Created by Yutong on 2/7/18.
>>>>>>> 9df9ba9f7d057b1abca818e78693585abfaa1f34
 */

public class Song {

<<<<<<< HEAD
    private String title;
    private int id;
    private String artist;
    private String album;

    public Song(String title, int id, String artist, String album){
        setTitle(title);
        setID(id);
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
        return album;
=======
    private String name;
    private String artist;
    private String album;

    public Song(String name, String artist, String album){
        this.name = name;
        this.artist = artist;
        this.album = album;
    }

    public String getName(){
        return this.name;
    }

    public String getArtist(){
        return this.artist;
    }

    public String getAlbum(){
        return this.album;
>>>>>>> 9df9ba9f7d057b1abca818e78693585abfaa1f34
    }
}
