package cse_110.flashback_player;

import org.junit.Test;

import java.time.OffsetDateTime;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void getScore_isCorrect() throws Exception {
        Song song = new Song("My Song", "Beverly", "My Album");
        song.setPreviousDate(OffsetDateTime.now());
        song.setCurrentDate(OffsetDateTime.of(2018, 2, 16, 16, 3, 30, 100, ZoneOffset(-08:00));
    }
}