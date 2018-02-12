package cse_110.flashback_player;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Duy on 2/9/2018.
 */

public class SongList {
    private final String RAWPATH = "app/src/main/res/raw/";

    private boolean isAlbumExist(String AlbumName) {
        if(AlbumName.isEmpty() || AlbumName.contains(" ")) return false;

        String path = RAWPATH + AlbumName;
        System.out.println(path);
        File file = new File(path);
        if(file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private File getAlbumFile(String AlbumName) {
        if(isAlbumExist(AlbumName)) {
            return new File(RAWPATH + AlbumName);
        } else {
            return null;
        }
    }

    //Return a list of song name (type string) given the name of album folder
    public List<String> songList(String AlbumName) {
        List<String> list = new ArrayList<String>();
        File AlbumFolder = getAlbumFile(AlbumName);
        File[] listOfSongs = AlbumFolder.listFiles();

        for (int i = 0; i < listOfSongs.length; i++) {
            String songName = listOfSongs[i].getName();
            if (listOfSongs[i].isFile() && isMp3File(songName)) {
                list.add(songName.substring(0,songName.length()-4));
            } else if (listOfSongs[i].isDirectory()) {
                list.addAll(songList(AlbumName+ "/" + songName));
            }
        }
        return list;
    }

    private boolean isMp3File(String songName) {
        if(songName.endsWith(".mp3")) {
            return true;
        } else {
            return false;
        }
    }


    /*public File getAlbumName(String AlbumName) {
        if(isAlbumExist(AlbumName)) {
            return new File("app/src/main/res/raw/" + AlbumName);
        } else {
            return null;
        }
    }

    public String encodeAlbumName(String AlbumName) {
        return AlbumName.replaceAll(" ", "");
    }
    */


    /*public static void main(String[] args) {
        SongList song = new SongList();
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        //System.out.println(song.isAlbumExist("newbestofkeatonsimons"));
        /*List<String> s  = song.songList("this_is_always");
        for(String a : s) {
            System.out.println(a);
        }
    }*/
}
