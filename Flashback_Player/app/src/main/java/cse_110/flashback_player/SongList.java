package cse_110.flashback_player;

import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import java.io.File;
import java.lang.reflect.Field;
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
    private static final String RAWPATH = "app/src/main/res/raw/";
    private Map<String,List<Song>> AlbumSongList;

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
        Set<Map.Entry<String,List<Song>>> set = AlbumSongList.entrySet();
        List<String> AlbumList = new ArrayList<>();
        for(Map.Entry<String,List<Song>> mem : set) {
            AlbumList.add(mem.getKey());
        }
        return AlbumList;
    }

    /*
     * getListOfSong : get the list of the song name file
     * Parameter: String AlbumName
     * Return: List<String>
     *         return empty List if there is no album
     */
    public List<Song> getListOfSong(String AlbumName) {
        if(isAlbumExist(AlbumName)) {
            return AlbumSongList.get(AlbumName);
        }
        return new ArrayList<Song>();
    }

    /*
     * getListOfAllSong: get the list of the song objects
     * Parameter: none
     * Return List<Song>
     */
    public List<Song> getAllsong() {
        List<Song> l = new ArrayList<>();
        for(Map.Entry<String, List<Song>> entry: AlbumSongList.entrySet()) {
            for(Song a : entry.getValue()) {
                l.add(a);
            }
        }
        return l;
    }

    //String All Song
    private List<Song> getAll() {
        List<Song> l = new ArrayList<>();
        for(Map.Entry<String, List<Song>> entry: AlbumSongList.entrySet()) {
            for(Song a : entry.getValue()) {
                l.add(a);
            }
        }
        return l;
    }


//  ---------------------------- HELPER METHOD BEGIN HERE -----------------------------------------
    private void generateAll() {
        AlbumSongList = new HashMap<String, List<Song>>();
        Field[] raw = cse_110.flashback_player.R.raw.class.getFields();
        //String s;
        List<Song> listOfSongs = new ArrayList<>();
        for (Field f : raw) {
            try {
                Map<String,String[]> da = Main2Activity.data;
                Song so = new Song(da.get(f.getName())[0],f.getInt(null),da.get(f.getName())[1],da.get(f.getName())[2]);
                listOfSongs.add(so);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Song a : listOfSongs) {
            String songName = a.getTitle() + ".mp3";
            if (isMp3File(songName)) {
                String album = a.getAlbum();
                if (AlbumSongList.isEmpty() || !AlbumSongList.containsKey(album)) {
                    ArrayList<Song> array = new ArrayList<>();
                    array.add(a);
                    AlbumSongList.put(album, array);
                } else {
                    AlbumSongList.get(album).add(a);
                }
            }
        }
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

//    public static void main(String[] args) {
//        SongList s = new SongList();
//        List<Song> l = s.getAllsong();
//        for(Song a : l) {
//            System.out.println(a.getID());
//        }
//        Field[] raw = cse_110.flashback_player.R.raw.class.getFields();
//        for (Field f : raw) {
//            try {
//                //System.out.println(f.get());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}

