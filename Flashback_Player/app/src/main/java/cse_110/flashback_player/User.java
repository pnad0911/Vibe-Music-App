package cse_110.flashback_player;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by winnieli on 3/4/18.
 */

public class User {
    private String name;
    private ArrayList<String> friendsList = new ArrayList<>();

    /* GETTER */
    public String getName(){return name;}

    public void setName(String name){this.name = name; }

    public ArrayList<String> getFriendlist(){
        return friendsList;
    }

    public void setFriendsList(ArrayList<String> friends){
        friendsList = friends;
    }

}