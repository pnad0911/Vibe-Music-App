package cse_110.flashback_player;

import android.location.Location;

import java.time.OffsetDateTime;
import java.util.ArrayList;

/**
 * Created by Yutong on 3/1/18.
 */

public class Friend{

    private String userId;
    private Location currLocation;
    private OffsetDateTime currTime;
    ArrayList<SongObserver> soArray = new ArrayList<SongObserver>();

    public Friend(String uid){
        userId = uid;
    }

    /* GETTER */
    public String getID(){ return userId; }
    public Location getLocation(){ return currLocation; }
    public OffsetDateTime getTime() { return currTime; }

    /* SETTER */
    public void setLocation(Location location){
        currLocation = location;
    }

    public void setTime(OffsetDateTime time){
        this.currTime = time;
    }

}
