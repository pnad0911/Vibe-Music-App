package tests;

import org.junit.Test;

import cse_110.flashback_player.SongList;

import static org.junit.Assert.assertEquals;

/**
 * Created by winnieli on 2/18/18.
 */

public class SongListUnitTests {

    @Test
    public void testGenerateAll(){
        SongList songList = new SongList();
        assertEquals(10,songList.getAllsong().size());

    }

}
