package cse_110.flashback_player;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Patrick on 3/4/2018.
 */

public class SongDownloadHelper {

    List<DownloadCompleteListener> listeners = new LinkedList<>();
    String url;
    String destination;
    DownloadManager downloadManager;
    AppCompatActivity context;
    private static final String TAG = "SongDownloadHelper";

    /**
     * Creates a SongDownloadHelper to download one song or album
     * @param url URL to download from
     * @param destination destination in filesystem
     * @param listener Class that listens for when download is complete
     * @param context AppCompatActivity to be downloaded in(typically pass the activity)
     */
    public SongDownloadHelper(String url, String destination, DownloadCompleteListener listener, AppCompatActivity context){
        this.url = url;
        this.destination = destination;
        this.context = context;
        listeners.add(listener);
        downloadManager = (DownloadManager)this.context.getSystemService(Context.DOWNLOAD_SERVICE);

    }

    private void startDownload(){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateListeners(url);
            }
        };
        request.setDestinationInExternalFilesDir(context ,null, destination);
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        downloadManager.enqueue(request);
        Log.d(TAG, "Started download from " + url);


    }

    private void updateListeners(String url){
        for(DownloadCompleteListener listener: listeners){
            listener.downloadCompleted(url);
        }
        Log.d(TAG, "Download completed");
    }

    public interface DownloadCompleteListener{
        public abstract void downloadCompleted(String url);
    }


}