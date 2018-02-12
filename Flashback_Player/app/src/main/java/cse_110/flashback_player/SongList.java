package cse_110.flashback_player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Duy on 2/9/2018.
 */

public class SongList {
    private final String RAWPATH = "app/src/main/res/raw/";
    private Map<String,List<String>> AlbumSongList;

    /* Constructor  */
    public SongList() {
        generateAll();
    }

    /*
     * getListOfAlbum : get the list of the album name
     * Return: List<String>
     *         return empty List if there is no album
     */
    public List<String> getListOfAlbum() {
        Set<Map.Entry<String,List<String>>> set = AlbumSongList.entrySet();
        List<String> AlbumList = new ArrayList<>();
        for(Map.Entry<String,List<String>> mem : set) {
            AlbumList.add(mem.getKey());
        }
        return AlbumList;
    }

    public List<String> getAllsong() {
        List<String> list = new ArrayList<String>();
        File AlbumFolder = new File(RAWPATH);
        File[] listOfSongs = AlbumFolder.listFiles();

        for(int i = 0; i < listOfSongs.length; i++) {
            String songName = listOfSongs[i].getName();
            if(listOfSongs[i].isFile() && isMp3File(RAWPATH+songName)) {
                list.add(songName);
            }
        }
        return list;
    }

    /*
     * getListOfSong : get the list of the song name file
     * Parameter: String AlbumName
     * Return: List<String>
     *         return empty List if there is no album
     */
    public List<String> getListOfSong(String AlbumName) {
        if(isAlbumExist(AlbumName)) {
            return AlbumSongList.get(AlbumName);
        }
        return new ArrayList<String>();
    }




    // --------------------- HELPER METHOD BEGIN HERE ----------------------------
    private void generateAll() {
        AlbumSongList = new HashMap<String,List<String>>();
        File AlbumFolder = new File(RAWPATH);
        File[] listOfSongs = AlbumFolder.listFiles();

        for(int i = 0; i < listOfSongs.length; i++) {
            String songName = listOfSongs[i].getName();
            if(listOfSongs[i].isFile() && isMp3File(songName)) {
                Song song = new Song(songName);
                String album = song.getAlbum();
                if (AlbumSongList.isEmpty() || !AlbumSongList.containsKey(album)) {
                    ArrayList<String> array = new ArrayList<>();
                    array.add(songName);
                    AlbumSongList.put(album,array);
                } else {
                    AlbumSongList.get(album).add(songName);
                }
            }
        }
        /*ArrayList<String> array = new ArrayList<>();
        array.add("aaa");array.add("bb b");array.add("c");array.add("dd");
        AlbumSongList.put("1",array);
        AlbumSongList.get("1").add("eeeeeee");
        ArrayList<String> array2 = new ArrayList<>();
        array2.add("abc");array2.add("bb b");array2.add("c");array2.add("dd");
        AlbumSongList.put("2",array2);
        ArrayList<String> array3 = new ArrayList<>();
        AlbumSongList.put("3",array3);*/
    }


    public boolean isAlbumExist(String AlbumName) {
        return AlbumSongList.containsKey(AlbumName);
    }

    private boolean isAlbumExistFolder(String AlbumName) {
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

    private List<String> songList(String AlbumName) {
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


    //-------------------Main for testing-------------------------

    /*public static void main(String[] args) {
        SongList song = new SongList();
        List<String> s  = song.getListOfAlbum();
        for(String a : s) {
            System.out.println(a);
        }
        List<String> s1  = song.getListOfSong("b");

    }*/
}

