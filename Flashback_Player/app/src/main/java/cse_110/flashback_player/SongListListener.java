package cse_110.flashback_player;

import java.util.List;
import java.util.Map;

/**
 * Created by Yutong on 3/12/18.
 */

public interface SongListListener {

    public void updateDisplay(List<Song> list);
    public void updateDisplay(Map<String, List<Song>> map, List<String> albumNames);
    public void updateDisplayUpcoming(List<Song> list);
}
