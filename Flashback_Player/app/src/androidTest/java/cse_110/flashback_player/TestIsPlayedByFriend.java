package cse_110.flashback_player;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Beverly Li on 3/16/2018.
 */

@RunWith(AndroidJUnit4.class)
public class TestIsPlayedByFriend {

    @Mock
    User user = new User();
    Song song1 = new Song();
    ArrayList<String> friends = new ArrayList<>();
    ArrayList<String> usersPlayed = new ArrayList<>();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {

        friends.add("BeverlyLi");
        friends.add("DuyPham");
        friends.add("YutongQiu");

        usersPlayed.add("BeverlyLi");

        song1.setUserNames(usersPlayed);
        user.setFriendsList(friends);

    }

 //   @Test
 /*   public void testisplayedbyfriend() {
       // Log.e(TAG,song1.getUserNames().get(0));
        Log.e(TAG,user.getFriendlist().get(0));
        assertEquals(true, song1.isPlayedByFriend());
    }
*/
}
