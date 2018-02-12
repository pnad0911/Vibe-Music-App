package cse_110.flashback_player;

import android.location.Location;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static java.sql.DriverManager.println;

/**
 * Created by Beverly Li on 2/10/2018.
 */

public class SongMetadata {

    String songPath;
    MediaMetadataRetriever mmr;

    public SongMetadata(String songPath, MediaMetadataRetriever mmr) {

        this.songPath = songPath;
        this.mmr = mmr;
        try {
            this.mmr.setDataSource(songPath);
        } catch (RuntimeException e) {
            System.out.println("failed to set data source");

        }
        System.out.println("after set data source");
        /*        try {
            mmr.setDataSource(songPath);
        } catch(RuntimeException e) {
            Log.e(TAG, "Illegal song path");
        }*/
    }

    // song location? (optional)
    public Location getLocation() {
        String stringLoc = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION);
        Location loc = new Location(stringLoc);
        return loc;
    }

    public String getSongName() {
        return mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
    }

    // get name of album that song is in
    public String getAlbumName() {
        return mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
    }

    //get name of artist of song
    public String getArtistName() {
        return mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
    }

    public byte[] getAlbumArt() {
        return mmr.getEmbeddedPicture();
    }

    public String getTrackNumber() {
        return mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
    }

    // optional
    public String getGenre() {
        return mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
    }

    // get year song was created or modified (optional)
    public String getYear() {
        return mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
    }
/*
    public static void main(String[] args) {
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            String path = "app/src/main/res/raw/after_the_storm.mp3";
            SongMetadata sm = new SongMetadata(path, mmr);
            String res = sm.getSongName();
            System.out.println(res);

        } catch (RuntimeException e) {
            System.out.println("catch");
        }

    }
*/
}

    /*
    List<String> songNames;
    private final String songPath = "app/src/main/res/raw/";
*/

    /*
    File[] songList;
    File[] albumSongs;

    // get all names of songs
    public List<String> getSongNames() {

        File songFile = new File(songPath);
        songList = songFile.listFiles();
        mmr.setDataSource(songPath);

        for (int i = 0; i < songList.length; i++) {
            mmr.setDataSource(songPath + songList[i]);
            if (songList[i].isDirectory()) {

                // check for albums under res/raw
                File album = new File(songPath + songList[i]);
                albumSongs = album.listFiles();

                // get songs under album
                for (int j = 0; j < albumSongs.length; j++) {
                    mmr.setDataSource(songPath + songList[i] + "/" + albumSongs[j]);
                    if (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO) != null) {
                        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                        songNames.add(title);
                    }
                }

                // check for songs under res/raw
            } else if (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO) != null) {
                String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                songNames.add(title);
            }
        }

        return songNames;

    }
*/



