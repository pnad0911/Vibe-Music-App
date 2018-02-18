package cse_110.flashback_player;

import android.location.Location;

import java.time.OffsetDateTime;
import android.content.Context;

>>>>>>> f4a7cb1493e8ca5be81b6bd4bc7051ab76a94dfb
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

        if(t1.getScore(location, time) != t2.getScore(location, time)){
            if(t1.getScore(location, time) > t2.getScore(location, time)){

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