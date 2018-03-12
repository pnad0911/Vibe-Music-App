package cse_110.flashback_player;

import java.util.ArrayList;

/**
 * Created by winnieli on 3/4/18.
 */

public class User {
    private String firstName;
    private String lastName;
    private ArrayList<Friend> friendsList = new ArrayList<Friend>();

    /* GETTER */
    public String getFirstName(){ return firstName; }
    public String getLastName(){ return lastName; }

    public void setFirstName(String name){
        this.firstName = name;
    }
    public void setLastName(String name){
        this.lastName = name;
    }


    public ArrayList<Friend> getFriendlist(){
        return friendsList;
    }

    public void setFriendsList(ArrayList<Friend> friends){
        friendsList = friends;
    }

}