package cse_110.flashback_player;
import android.location.Location;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import static android.location.LocationManager.GPS_PROVIDER;
import static junit.framework.Assert.assertEquals;
/**
 * Created by Beverly Li on 3/16/2018.
 */
@RunWith(AndroidJUnit4.class)
public class TestTimeLocation {
    @Mock
    Location loc1 = new Location(GPS_PROVIDER);
    Location loc2 = new Location(GPS_PROVIDER);
    Location loc3 = new Location(GPS_PROVIDER);
    OffsetDateTime time1 = OffsetDateTime.parse("2018-03-14T10:15:30-08:00");
    OffsetDateTime time2 = time1.minusDays(2);
    OffsetDateTime time3 = time1.minusDays(9);
    OffsetDateTime time4 = time1.minusDays(3);
    Song song1 = new Song();
    Song song2 = new Song();
    ArrayList<HashMap<String,String>> loc = new ArrayList<>();
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Before
    public void setUp() {
        loc1.setLatitude(200);
        loc1.setLongitude(200);
        loc2.setLatitude(500);
        loc2.setLongitude(500);
        song1.addLocation(loc1);
        song2.addLocation(loc2);
        song1.setDate(time1);
        song2.setDate(time2);
    }
    @Test
    public void testLocation() {
        assertEquals(loc1.getLatitude(),song1.previousLocation().first);
        assertEquals(loc1.getLongitude(),song1.previousLocation().second);
    }
    @Test
    public void testTime() {
        assertEquals(time1.toString(), song1.getDate().toString());
        assertEquals(time2.toString(), song2.getDate().toString());
    }
}