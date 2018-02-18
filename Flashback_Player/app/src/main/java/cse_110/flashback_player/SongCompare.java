package cse_110.flashback_player;

import android.content.Context;

import java.util.Comparator;

/**
 * Created by Patrick on 2/15/2018.
 */

public class SongCompare<T extends Song> implements Comparator<T> {
    private static final double DISTANCE = 1000;
    @Override
    public int compare(T t1, T t2) {
        Context applicationContext =  Main2Activity.getContextOfApplication();
        if(t1.getScore() != t2.getScore()){
            if(t1.getScore() > t2.getScore()){
                return -1;
            }
            else{
                return 1;
            }
        }

        if(t1.getLikedStatus() != t2.getLikedStatus()){
            if(t1.getLikedStatus() == true){
                return -1;
            }
            else{
                return 1;
            }
        }

        if(t1.getPreviousDate(applicationContext).isAfter(t2.getPreviousDate(applicationContext))){
            return -1;
        }

        return 1;
    }
    Boolean a = null;
}