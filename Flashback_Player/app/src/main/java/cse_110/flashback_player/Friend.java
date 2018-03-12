package cse_110.flashback_player;

import android.location.Location;

import java.time.OffsetDateTime;
import java.util.ArrayList;

/**
 * Created by Yutong on 3/1/18.
 */

public class Friend{

    private String lastName;
    private String firstName;

    /* GETTER */
    public String getLastName(){ return lastName; }

    public String getFirstName(){ return firstName; }

    public void setLastName(String name){
        this.lastName = name;
    }

    public void setFirstName(String name){
        this.firstName = name;
    }

}
