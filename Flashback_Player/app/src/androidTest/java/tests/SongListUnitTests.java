package tests;

import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import cse_110.flashback_player.Main2Activity;
import cse_110.flashback_player.SongList;

import static org.junit.Assert.assertEquals;

    /**
     * Created by winnieli on 2/18/18.
     */

    public class SongListUnitTests {

        private SongList songList;

        @Rule
        public ActivityTestRule<Main2Activity> main2Activity = new ActivityTestRule<Main2Activity>(Main2Activity.class);

        @Before
        public void setUp(){
            songList = new SongList();
        }
        @Test
        public void testGenerateAll(){
            assertEquals(10,songList.getAllsong().size());
            String albumName = songList.getListOfAlbum().get(0);
            assertEquals("Take Yourself Too Seriously", albumName);
            assertEquals(2, songList.getListOfSong(albumName).size());
        }

        @Test
        public void testIsAlbumExist(){
            String albumName = songList.getListOfAlbum().get(0);
            assertEquals(true, songList.isAlbumExist(albumName));
            assertEquals(false, songList.isAlbumExist("123"));
            albumName = songList.getListOfAlbum().get(4);
            assertEquals(true, songList.isAlbumExist(albumName));
        }

    }