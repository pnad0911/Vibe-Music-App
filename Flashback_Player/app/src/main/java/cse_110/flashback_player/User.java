package cse_110.flashback_player;

import android.location.Location;

import java.time.OffsetDateTime;
import java.util.ArrayList;

/**
 * Created by winnieli on 3/4/18.
 */

public class User {
    private String userId;
    private Location currLocation;
    private OffsetDateTime currTime;
    private ArrayList<Friend> friendsList = new ArrayList<Friend>();

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

    public ArrayList<Friend> getFriendlist(){

        return friendsList;
    }


}
