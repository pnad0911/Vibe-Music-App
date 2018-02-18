package cse_110.flashback_player;

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

        if(t1.getScore(location, time) != t2.getScore(location, time)){
            if(t1.getScore(location, time) > t2.getScore(location, time)){
                return -1;
            }
            else{
                return 1;
            }
        }

        if(t1.getIsFavorite() != t2.getIsFavorite()){
            if(t1.getIsFavorite()){
                return -1;
            }
            else{
                return 1;
            }
        }

        if(t1.getPreviousDate().isAfter(t2.getPreviousDate())){
            return -1;
        }

        return 1;
    }
}