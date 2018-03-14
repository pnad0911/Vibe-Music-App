package cse_110.flashback_player;

import android.location.Location;
import android.util.Pair;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static cse_110.flashback_player.logIn.user;
import static java.time.OffsetDateTime.parse;

/**
 * Created by Beverly Li on 3/11/2018.
 */

//public class WeightSystem extends Song {
/*
    private Location location;
    private OffsetDateTime time;

    private final double locRange = 1000; // feet
    private final double latToFeet = 365228;
    private final double longToFeet = 305775;

    public void WeightSystem(Location location, OffsetDateTime time) {
        this.location = location;
        this.time = time;
    }*/
   // @Override
/*    public double getScore(Location userLocation, OffsetDateTime presentTime) {
        double locScore = getLocationScore(userLocation);
        double weekScore = getWeekScore(presentTime);
        double friendScore = getFriendScore();
        return locScore + weekScore + friendScore;
    }
*/
    /**
     * helper for getScore
     * @return location score
     */
   // @Override
  /*  public double getLocationScore(Location userLocation) {

        if (userLocation == null) {
            return 0;
        }

        double prevFeetLat = Double.parseDouble(previousLocation().first) * latToFeet;
        double prevFeetLong = Double.parseDouble(previousLocation().second) * longToFeet;
        double currFeetLat = userLocation.getLatitude() * latToFeet;
        double currFeetLong = userLocation.getLongitude() * longToFeet;
        double distance = Math.sqrt(Math.pow(currFeetLat - prevFeetLat, 2) +
                Math.pow(currFeetLong - prevFeetLong, 2));
        // check if song was played within 1000 ft
        if (distance > locRange) {
            return 0;
        }
        return 102;
    }
*/
    /**
     * helper for getScore
    // * @param presentTime
     * @return week score
     */
    //@Override
  /*  public double getWeekScore(OffsetDateTime presentTime) {
        OffsetDateTime playedTime = OffsetDateTime.parse(getDate());
        if (playedTime == null) {
            return 0;
        }
        OffsetDateTime lastWeekBegin = presentTime.minusDays(7);
        // Monday (value 1) to Sunday (value 7) is a week
        while (lastWeekBegin.getDayOfWeek().getValue() != 1) {
            lastWeekBegin = lastWeekBegin.minusDays(1);
        }
        for (int i = 0; i < 7; i++) {
            if (lastWeekBegin.getYear() == playedTime.getYear() &&
                    lastWeekBegin.getDayOfYear() == playedTime.getDayOfYear()) {
                return 101;
            }
            lastWeekBegin = lastWeekBegin.plusDays(1);
        }
        return 0;
    }
*/
    /**
     * helper for getScore
     * @return friend score
     */
   // @Override
  /*  public double getFriendScore() {
        if (isPlayedByFriend()) {
            return 100;
        }
        return 0;
    }
*/
    /**
     * helper for getFriendScore
     * @return true if song was played by friend, false otherwise
     */
 //   @Override
  /*  public boolean isPlayedByFriend() {
        ArrayList<String> usersPlayedSong = getUserNames();
        ArrayList<Pair<String,String>> friends = user.getFriendlist();
        for (String username : usersPlayedSong) {
            for (Pair<String,String> friend : friends) {
                if (username.equals(friend.second + friend.first)) {
                    return true;
                }
            }
        }
        return false;
    }
*/
//}
