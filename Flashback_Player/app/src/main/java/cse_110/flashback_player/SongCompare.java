package cse_110.flashback_player;

import java.util.Comparator;

/**
 * Created by Patrick on 2/15/2018.
 */

public class SongCompare<T extends Song> implements Comparator<T extends Song> {
    private static final double DISTANCE = 1000;
    @Override
    public int compare(T t1, T t2) {

        if(t1.getScore() != t2.getScore()){
            if(t1.getScore() > t2.getScore()){
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

        if(t1.getDate().after(t2.getDate())){
            return -1;
        }

        return 1;
    }
}
