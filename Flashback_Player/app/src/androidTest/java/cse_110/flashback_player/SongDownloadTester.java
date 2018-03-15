//package cse_110.flashback_player;
//
//import android.os.Environment;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.util.Log;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.Assert;
//import org.junit.runner.RunWith;
//
//import static junit.framework.Assert.assertEquals;
//
///**
// * Created by Patrick on 3/6/2018.
// */
//
//@RunWith(AndroidJUnit4.class)
//public class SongDownloadTester {
//
//    boolean finished = false;
//    SongDownloadHelper helper;
//    private static final String url = "https://s3-us-west-1.amazonaws.com/cse110-flashbackplayer/back_east.mp3";
//    @Rule
//    public ActivityTestRule<NormalActivity> mainActivity = new ActivityTestRule<NormalActivity>(NormalActivity.class);
//
//    @Before
//    public void setup(){
//
//    }
//
//    @Test
//    public void testSongDownload(){
//        String dest = mainActivity.getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
//        System.err.println(dest);
//        helper = new SongDownloadHelper(url, dest, new SongDownloadHelper.DownloadCompleteListener() {
//            @Override
//            public void downloadCompleted(String url) {
//                Log.e("TEST", url);
//                System.err.println(url);
//                setFinished();
//            }
//        }, mainActivity.getActivity());
//        while(!finished);
//        assertEquals(true,true);
//    }
//
//
//    private void setFinished(){
//        finished = true;
//    }
//
//}

