package cse_110.flashback_player;

/**
 * Created by Yutong on 2/7/18.
 */

public class Song {

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
    }
}
