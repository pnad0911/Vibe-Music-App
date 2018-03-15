package cse_110.flashback_player;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Patrick on 3/4/2018.
 */

public class SongDownloadHelper {

    List<DownloadCompleteListener> listeners = new LinkedList<>();
    String url;
    String destination = Environment.DIRECTORY_DOWNLOADS;
    DownloadManager downloadManager;
    AppCompatActivity context;
    private static final String TAG = "SongDownloadHelper";

    /**
     * Creates a SongDownloadHelper to download one song or album
     *
     * @param listener    Class that listens for when download is complete
     * @param context     AppCompatActivity to be downloaded in(typically pass the activity)
     */
    public SongDownloadHelper(String url, DownloadCompleteListener listener, AppCompatActivity context) {
        this.url = url;
        this.context = context;
        listeners.add(listener);
        downloadManager = (DownloadManager) this.context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public void startDownload() {
//        this.url = address;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("FIXED.mp3");
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateListeners(url);
                context.unregisterReceiver(this);
            }
        };
        //destination += url;
        if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission is granted");

        }
        else{
            Log.e(TAG, "no permissions");
            ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }

        Log.d(TAG,destination);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "temp.mp3");
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        downloadManager.enqueue(request);
        Log.d(TAG, "Started download from " + url);
    }

    private void updateListeners(String url) {
        Cursor c = downloadManager.query(new DownloadManager.Query());
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                // process download
                Log.d(TAG, "Title is: " + c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE)));
                Log.d(TAG, "destination is: "+ c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
                // get other required data by changing the constant passed to getColumnIndex
            }
        }
        for (DownloadCompleteListener listener : listeners) {
            listener.downloadCompleted(url);
        }
        Log.d(TAG, "Download completed");
    }

    public interface DownloadCompleteListener {
        public abstract void downloadCompleted(String url);
    }


}