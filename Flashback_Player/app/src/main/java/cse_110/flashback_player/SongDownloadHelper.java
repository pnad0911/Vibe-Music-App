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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Patrick on 3/4/2018.
 */

public class SongDownloadHelper {

    List<DownloadCompleteListener> listeners = new LinkedList<>();
    String url;
    String destination = Environment.DIRECTORY_DOWNLOADS;
    DownloadManager downloadManager;
    AppCompatActivity context;
    Song song;
    private String extension = "";
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
    public SongDownloadHelper(String url, DownloadCompleteListener listener, AppCompatActivity context, Song song) {
        this.song = song;
        Log.e("downLoadHelper", "DatabaseKey is: " + song.getDatabaseKey());
        this.url = url;
        this.context = context;
        listeners.add(listener);
        downloadManager = (DownloadManager) this.context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public void startDownload() {
//        this.url = address;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        extension = Uri.parse(url).getLastPathSegment().substring(Uri.parse(url).getLastPathSegment().lastIndexOf('.'));
        request.setTitle("FIXED" + extension);
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
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "temp" + extension);
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
                String filename = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                String realDest = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                String realPath = Uri.parse(realDest).getPath();
                Log.d(TAG, "Title is: " + c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE)));
                Log.d(TAG, "destination is: "+ c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));


                Log.d("TESTING", realPath.substring(realPath.lastIndexOf('/')));
                //unpack if zip file
                if(extension.equals(".zip")){

                    Log.d("TESTING", realPath.substring(0, realPath.lastIndexOf('/')));
                    try{

                        unzip(new File(realPath), new File(realPath.substring(0, realPath.lastIndexOf('/'))));
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }

            }
        }
        for (DownloadCompleteListener listener : listeners) {
            listener.downloadCompleted(url);
            listener.downloadCompleted(this.song);
        }
        Log.d(TAG, "Download completed");
    }

    public interface DownloadCompleteListener {
        public abstract void downloadCompleted(String url);
        public abstract void downloadCompleted(Song song);
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        Log.d("WTF", zipFile.getAbsolutePath());
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }
    }


}