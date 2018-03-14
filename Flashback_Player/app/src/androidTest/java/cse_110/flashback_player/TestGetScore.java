package cse_110.flashback_player;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;

import static android.location.LocationManager.GPS_PROVIDER;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.OffsetDateTime;
import java.util.regex.Pattern;

/**
 * Created by Beverly Li on 3/13/2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class TestGetScore {

    @Mock
    Song song1;
    Song song2;
    Song song3;
    Location loc1;
    Location loc2;
    Location loc3;
    OffsetDateTime time1;
    OffsetDateTime time2;
    OffsetDateTime time3;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.when(TextUtils.isEmpty(any(CharSequence.class))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                CharSequence a = (CharSequence) invocation.getArguments()[0];
                return !(a!= null && a.length() > 0);
            }
        });
        song1 = new Song();
        song2 = new Song();
        song3 = new Song();

        time1 = OffsetDateTime.parse("2007-12-03T10:15:30-08:00");
        time2 = OffsetDateTime.parse("2007-12-03T10:15:30-08:00");
        time3 = OffsetDateTime.parse("2007-12-03T10:15:30-08:00");

        loc1 = new Location(GPS_PROVIDER);
        loc2 = new Location(GPS_PROVIDER);
        loc3 = new Location(GPS_PROVIDER);

        loc2.setLatitude(100);
        loc2.setLongitude(100);

        loc3.setLatitude(100);
        loc3.setLongitude(100);

        song1.addLocation(loc1);
        song1.setDate(time1);

        song2.addLocation(loc2);
        song2.setDate(time2);

        song3.addLocation(loc3);
        song3.setDate(time3);

    }

    @Test
    public void testGetLocationScore() {
        assertEquals(0.0,song1.getLocationScore(null));
        assertEquals(102.0,song2.getLocationScore(loc1));
    }
}
