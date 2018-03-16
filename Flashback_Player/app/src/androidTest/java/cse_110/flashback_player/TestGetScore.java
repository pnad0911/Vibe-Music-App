package cse_110.flashback_player;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.location.LocationManager.GPS_PROVIDER;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.time.OffsetDateTime;
import java.util.regex.Pattern;

/**
 * Created by Beverly Li on 3/13/2018.
 */

@RunWith(AndroidJUnit4.class)
public class TestGetScore {

    @Mock
    Song song1 = new Song();
    Song song2 = new Song();
    Song song3 = new Song();
    Location loc1 = new Location(GPS_PROVIDER);
    Location loc2 = new Location(GPS_PROVIDER);
    Location loc3 = new Location(GPS_PROVIDER);
    OffsetDateTime time1 = OffsetDateTime.parse("2018-03-14T10:15:30-08:00");
    OffsetDateTime time2 = time1.minusDays(2);
    OffsetDateTime time3 = time1.minusDays(9);

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {

        loc2.setLatitude(100);
        loc2.setLongitude(100);

        loc3.setLatitude(100.1);
        loc3.setLongitude(100.1);

        song1.setLocation(loc1);
        song1.setDate(time1);
        song1.addLocation(loc1);

        song2.setLocation(loc2);
        song2.setDate(time2);
        song2.addLocation(loc2);

        song3.setLocation(loc3);
        song3.setDate(time3);
        song3.addLocation(loc3);

    }

    @Test
    public void testGetLocationScore() {
        assertEquals(0.0,song1.getLocationScore(null));
        assertEquals(102.0,song2.getLocationScore(loc2));
        assertEquals(102.0, song3.getLocationScore(loc2));
        assertEquals(0.0, song3.getLocationScore(loc1));
    }

    @Test
    public void testGetWeekScore() {
        assertEquals(0.0, song1.getWeekScore(null));
        assertEquals(0.0, song1.getWeekScore(time3));
        assertEquals(0.0, song1.getWeekScore(time1));
        assertEquals(101.0, song3.getWeekScore(time1));


    }
}
