package tests;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import cse_110.flashback_player.LibraryActivity;
import cse_110.flashback_player.SongList;

import static org.junit.Assert.assertEquals;

    /**
     * Created by winnieli on 2/18/18.
     */

    public class SongListUnitTests {

        private SongList songList;

        @Rule
        public ActivityTestRule<LibraryActivity> main2Activity = new ActivityTestRule<LibraryActivity>(LibraryActivity.class);

        @Before
        public void setUp(){
            songList = new SongList(main2Activity.getActivity());
        }
        @Test
        public void testGenerateAll(){
            assertEquals(3,songList.getAllsong().size());
            String albumName = songList.getListOfAlbum().get(0);
            assertEquals("I Will Not Be Afraid (A Sampler)", albumName);
            assertEquals(2, songList.getListOfSong(albumName).size());
        }

        @Test
        public void testIsAlbumExist(){
            String albumName = songList.getListOfAlbum().get(0);
            assertEquals(true, songList.isAlbumExist(albumName));
            assertEquals(false, songList.isAlbumExist("123"));
            albumName = songList.getListOfAlbum().get(1);
            assertEquals(true, songList.isAlbumExist(albumName));
        }

    }
