package cse_110.flashback_player;

import android.location.Location;
import java.time.OffsetDateTime;

/**
 * Created by Yutong on 3/1/18.
 */

public interface SongObserver {
    void update(Location location, OffsetDateTime time);
}
