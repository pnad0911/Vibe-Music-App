package cse_110.flashback_player;

import android.content.Context;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.OffsetDateTime;
import java.util.Comparator;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestSongCompare {

    Location loc;
    OffsetDateTime date;
    SongCompare<Song> comparator;
    @Before
    public void begin(){
        loc = new Location("");
        date = OffsetDateTime.now();
        comparator = new SongCompare<Song>(loc, date);
    }

    @Test
    public void testCompare(){

    }
}

