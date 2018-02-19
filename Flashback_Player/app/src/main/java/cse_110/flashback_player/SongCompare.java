package cse_110.flashback_player;

import android.content.Context;

import android.location.Location;

import java.time.OffsetDateTime;
import java.util.Comparator;

/**
 * Created by Patrick on 2/15/2018.
 */

public class SongCompare<T extends Song> implements Comparator<T> {
    private static final double DISTANCE = 1000;
    private Location location;
    private OffsetDateTime time;

    public SongCompare(Location location, OffsetDateTime time){
        this.location = location;
        this.time = time;
    }

    @Override
    public int compare(T t1, T t2) {
        Context applicationContext =  Main2Activity.getContextOfApplication();
        if (location == null || time == null) {
            return 0;
        }
        if(t1.getScore(location,time) != t2.getScore(location,time)){
            if(t1.getScore(location,time) > t2.getScore(location,time)){

                return -1;
            }
            else{
                return 1;
            }
        }

        if(t1.getSongStatus() != t2.getSongStatus()){
            if(t1.getSongStatus() == 1){
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

}


