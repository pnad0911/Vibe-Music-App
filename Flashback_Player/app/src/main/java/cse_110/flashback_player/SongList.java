package cse_110.flashback_player;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
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
    private final String DOWNLOADPATH = Environment.DIRECTORY_DOWNLOADS;
    private Map<String, List<Song>> AlbumSongList = new HashMap<>();
    private ArrayList<Song> songs = new ArrayList<>();
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
        return new ArrayList<>();
    }

    /*
     * getListOfAllSong: get the list of the song objects
     * Parameter: none
     * Return List<Song>
     */
    public List<Song> getAllsong() {
//        List<Song> l = new ArrayList<>();
//        for (Map.Entry<String, List<Song>> entry : AlbumSongList.entrySet()) {
//            for (Song a : entry.getValue()) {
//                l.add(a);
//            }
//        }
//        return l;
        return songs;
    }

    /*
    * Refresh the song list, find the newly downloaded song and assign url to that song
    * Parameter: new url */
    public void refresh(String url){
        generateAll();
        for (Song s : songs){
            if (s.getSongUrl().equals("")){
                s.setSongUrl(url, activity.getApplicationContext());
            }
        }
    }

    //  ---------------------------- HELPER METHOD BEGIN HERE -----------------------------------------
    private void generateAll() {
//        Field[] raw = cse_110.flashback_player.R.raw.class.getFields();
        File path = Environment.getExternalStoragePublicDirectory(DOWNLOADPATH);
        List<Song> listOfSongs = new ArrayList<>();
        File[] files = path.listFiles();
        if (files!=null) {
            for (File f : files) {
                try {
                    if (isMp3File(f)) {
                        String filePath = f.getAbsolutePath();
                        Song so = new Song(data.get(filePath)[0], data.get(filePath)[1], data.get(filePath)[2], filePath, true);
                        so.setSongUrl(so.getSongUrl(activity.getApplicationContext()));
                        songs.add(so);

                        // maintain album list
                        String album = so.getAlbum();
                        if (AlbumSongList.isEmpty() || !AlbumSongList.containsKey(album)) {
                            ArrayList<Song> array = new ArrayList<>();
                            array.add(so);
                            AlbumSongList.put(album, array);
                        } else {
                            AlbumSongList.get(album).add(so);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    private boolean isMp3File(File file) {
        if (file.getAbsolutePath().endsWith(".mp3")) {
            return true;
        } else {
            return false;
        }
    }

    public void getData() {
        data = new HashMap<>();
//        Field[] raw = cse_110.flashback_player.R.raw.class.getFields();
        File path = Environment.getExternalStoragePublicDirectory(DOWNLOADPATH);
        Log.println(Log.ERROR, "File Path", "FILE PATH is: "+ path.toString());
        File[] fileArray = path.listFiles();
        if (fileArray != null) {
            for (File f : fileArray) {
                try {
//                AssetFileDescriptor afd = activity.getResources().openRawResourceFd(f.getInt(null));
//                mmr.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    if (isMp3File(f)) {
                        FileInputStream fileInputStream = new FileInputStream(f);
                        FileDescriptor fd = fileInputStream.getFD();
                        mmr.setDataSource(fd);
                        String al = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                        String ti = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                        String ar = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                        String[] list = new String[3];
                        list[0] = ti;
                        list[1] = ar;
                        list[2] = al;
                        data.put(f.getAbsolutePath(), list);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}




