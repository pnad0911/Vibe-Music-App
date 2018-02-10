package cse_110.flashback_player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duy on 2/9/2018.
 */

public class AlbumList {
    private final String RAWPATH = "app/src/main/res/raw/";

    //Return a list of album name (type string)
    public List<String> AlbumL() {
        return helperList("");
    }

    private List<String> helperList(String dir){
        String path = RAWPATH + dir;
        List<String> list = new ArrayList<String>();
        File RawFolder = new File(path);
        File[] listOfAlbums = RawFolder.listFiles();
        if(isNotEmpty(dir)) {
            for (int i = 0; i < listOfAlbums.length; i++) {
                String AlbumName = listOfAlbums[i].getName();
                if (listOfAlbums[i].isDirectory() && !list.contains(AlbumName)) {
                    list.add(AlbumName);
                    list.addAll(helperList(dir+"/" + AlbumName + "/"));
                }
            }
        }
        return list;
    }


    private boolean isNotEmpty(String p) {
        File file = new File(RAWPATH + p);
        if(file.list().length>0){
            return true;
        } else {
            return false;
        }
    }

    /*public static void main(String[] args) {
        AlbumList song = new AlbumList();
        List<String> s  = song.AlbumL();
        for(String a : s) {
            System.out.println(a);
        }
    }*/
}
