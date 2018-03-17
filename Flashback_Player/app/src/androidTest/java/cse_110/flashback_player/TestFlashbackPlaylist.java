package cse_110.flashback_player;
import android.content.Intent;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.jetty.servlet.Context;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import static android.location.LocationManager.GPS_PROVIDER;
import static junit.framework.Assert.assertEquals;
/**
 * Created by Daniel on 3/16/2018.
 */
@RunWith(AndroidJUnit4.class)

public class TestFlashbackPlaylist {
    VibeActivity vibeActivity;
    List<Song> songList;
    List<Song> resultList;
    @Rule
    public ActivityTestRule<VibeActivity> mainActivity = new ActivityTestRule<VibeActivity>(VibeActivity.class);
    @Before
    public void setUp() {
        Intent refresh = new Intent(InstrumentationRegistry.getContext(), VibeActivity.class);
        mainActivity.launchActivity(refresh);
        songList = new ArrayList<>();
        // artists
        String patrick = "bill_killer";
        String duy = "D Bawss";
        String yutong = "AAA";
        String beverly = "beverley";
        String winnie = "Big bully";
        String daniel = "汤圆";
        // albums
        String mygreatestalbum = "My Greatest Album";
        String mygreatestsingle = "My Greatest Single";
        // titles
        String berry = "Berry";
        String strawberry = "Strawberry";
        String ironMan = "Iron Man";
        String legion = "Legion";
        String singlereadytomingle = "Single Ready To Mingle";
        String goodsong = "Good Song";
        String bestsong = "Best Song";
        // paths
        String path = "THIS IS A PATH";
        // time
        Time dummyT1 = new Time(1);
        // location
        Location dummyL1 = new Location(GPS_PROVIDER);
        // songs
        Song berry_song = new Song(berry, patrick, mygreatestalbum, path, true);
        Song strawberry_song = new Song(strawberry, duy, mygreatestalbum, path, true);
        Song ironMan_song = new Song(ironMan, yutong, mygreatestsingle, path, true);
        Song legion_song = new Song(legion, beverly, mygreatestsingle, path, true);
        Song singlereadytomingle_song = new Song(singlereadytomingle, duy, mygreatestsingle, path, true);
        Song goodsong_song = new Song(goodsong, winnie, mygreatestalbum, path, true);
        Song bestsong_song = new Song(bestsong, daniel, mygreatestsingle, path, true);
        songList.add(berry_song);
        songList.add(strawberry_song);
        songList.add(ironMan_song);
        songList.add(legion_song);
        songList.add(singlereadytomingle_song);
        songList.add(goodsong_song);
        songList.add(bestsong_song);
        // counter
        int i = 0;
        for (Song song : songList) {
            song.addLocation(dummyL1);
            song.setDate(dummyT1);
            if (i % 2 == 0) {
                vibeActivity.vibePlaylist.neutralSong(song);
            } else {
                vibeActivity.vibePlaylist.likeSong(song);
            }
            ++i;
        }
        resultList = vibeActivity.vibePlaylist.getVibeSong();
    }
    @Test
    public void testGetVibeSong() {
        assertEquals(resultList.size(), 7);
        for (Song song : songList) {
            assertEquals(resultList.contains(song), true);
        }
    }
    @Test
    public void testLikeSong() {
        Song song = songList.get(songList.size()-1);
        assertEquals(vibeActivity.vibePlaylist.getSongStatus(song),0);
        vibeActivity.vibePlaylist.likeSong(song);
        assertEquals(vibeActivity.vibePlaylist.getSongStatus(song),1);
        for (int i = 0; i < resultList.size() - 1; ++i) {
            assertEquals(vibeActivity.vibePlaylist.getSongStatus(resultList.get(i)) >=
                    vibeActivity.vibePlaylist.getSongStatus(resultList.get(i+1)),true);
        }
    }
    @Test
    public void testDislikeSong() {
        Song song = songList.get(0);
        assertEquals(resultList.contains(song), true);
        VibeActivity.vibePlaylist.dislikeSong(song);
        assertEquals(vibeActivity.vibePlaylist.getSongStatus(song), -1);
        assertEquals(resultList.contains(song), false);
    }
    @Test
    public void testNeutralSong() {
        Song song = songList.get(0);
        assertEquals(song.getSongStatus(), 1);
        VibeActivity.vibePlaylist.neutralSong(song);
        assertEquals(song.getSongStatus(),0);
    }
    @Test
    public void testAddSong() {
        // artists
        String bill = "piazza";
        // albums
        String bill_album = "Best Song Of All Time";
        // titles
        String design = "Design Pattern";
        // paths
        String path = "THIS IS A PATH";
        // time
        Time dummyT1 = new Time(1);
        // location
        Location dummyL1 = new Location(GPS_PROVIDER);
        // songs
        Song bill_song = new Song(design, bill, bill_album, path, true);
        assertEquals(resultList.contains(bill_song), false);
        VibeActivity.vibePlaylist.addSong(bill_song);
        assertEquals(resultList.contains(bill_song), true);
    }
    @Test
    public void testGetSongStatus() {
        assertEquals(VibeActivity.vibePlaylist.getSongStatus(resultList.get(0)), resultList.get(0).getSongStatus());
    }
}