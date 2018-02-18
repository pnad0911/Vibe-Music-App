package cse_110.flashback_player;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.*;

/**
 * Created by Daniel to test FlashbackPlayer.
 */
public class FlashbackPlaylistTest {

    private FlashbackPlaylist playlist;

    @Before
    public void initialize() {
        playlist = new FlashbackPlaylist();
        // Specific date time from LocalDateTime with an offset
        OffsetDateTime dateTime = OffsetDateTime.of(LocalDateTime.of(1111, 11, 11, 11, 11),
                ZoneOffset.ofHoursMinutes(6, 30));
        // Specific location
        System.out.println(dateTime);

    }

    @Test
    public void testGetFlashBackMode() throws Exception {
        assertEquals(1, 1);
    }

    @Test
    public void testLikeSong() {
    }

    @Test
    public void testDislikeSong() {

    }

    @Test
    public void testNeutralSong() {

    }

    @Test
    public void testSongCurrentlyLiked() {
        
    }
}