package cse_110.flashback_player;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Duy on 2/9/2018.
 */

public class SongList {
    private final String RAWPATH = "app/src/main/res/raw/";
    private Map<String, List<Song>> AlbumSongList;
    private Map<String,String[]> data;
    private Activity activity;
    private MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    /* Constructor  */
    public SongList(Activity a) {
        activity = a;
        getData();
        generateAll();
    }

    /*
     * getListOfAlbum : get the list of the album name
     * Return: List<String>
     *         return empty List if there is no album
     */
    public List<String> getListOfAlbum() {
        Set<Map.Entry<String, List<Song>>> set = AlbumSongList.entrySet();
        List<String> AlbumList = new ArrayList<>();
        for (Map.Entry<String, List<Song>> mem : set) {
            AlbumList.add(mem.getKey());
        }
        return AlbumList;
    }
    public Map<String, List<Song>> getB() {
        return AlbumSongList;
    }
    /*
     * getListOfSong : get the list of the song name file
     * Parameter: String AlbumName
     * Return: List<String>
     *         return empty List if there is no album
     */
    public List<Song> getListOfSong(String AlbumName) {
        if (isAlbumExist(AlbumName)) {
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
        for (Map.Entry<String, List<Song>> entry : AlbumSongList.entrySet()) {
            for (Song a : entry.getValue()) {
                l.add(a);
            }
        }
        return l;
    }

    //  ---------------------------- HELPER METHOD BEGIN HERE -----------------------------------------
    private void generateAll() {
        AlbumSongList = new HashMap<String, List<Song>>();
        Field[] raw = cse_110.flashback_player.R.raw.class.getFields();
        List<Song> listOfSongs = new ArrayList<>();
        for (Field f : raw) {
            try {
                Song so = new Song(data.get(f.getName())[0], f.getInt(null), data.get(f.getName())[1], data.get(f.getName())[2]);
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
        if (AlbumName.isEmpty() || AlbumName.contains(" ")) return false;
        String path = RAWPATH + AlbumName;
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isMp3File(String songName) {
        if (songName.endsWith(".mp3")) {
            return true;
        } else {
            return false;
        }
    }

    public void getData() {
        data = new HashMap<>();
        Field[] raw = cse_110.flashback_player.R.raw.class.getFields();
        for (Field f : raw) {
            try {
                AssetFileDescriptor afd = activity.getResources().openRawResourceFd(f.getInt(null));
                mmr.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                String al = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                String ti = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String ar = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                String[] list = new String[3];
                list[0] = ti;list[1] = ar;list[2] = al;
                data.put(f.getName(),list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


