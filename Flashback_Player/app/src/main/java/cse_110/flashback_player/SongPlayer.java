package cse_110.flashback_player;


import android.*;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.time.Duration;
import java.time.Instant;

import com.google.android.gms.location.LocationServices;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import static android.os.UserHandle.readFromParcel;



/**
 * Created by Patrick on 2/7/2018.
 */

public class SongPlayer {

    private MediaPlayer mediaPlayer;
    private AppCompatActivity activity;
    private Song nextSong;
    private boolean paused = false;
    private List<SongPlayerCallback> callbackList;
   // private int paused = 0;
    private Double loc_lat;
    private Double loc_long;
    private Date date;
    private Bundle bundle;
    private Intent intent;
    private OffsetDateTime timestamp;
    private LocalDateTime ldt;

    /**
     * Creates a new SongPlayer object attached to the given activity
     * @param activity Activity this SongPlayer is attached to.
     */
    public SongPlayer(AppCompatActivity activity){
        callbackList = new LinkedList<>();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                play(nextSong); //TODO change if mediaplayer is choppy after songs
                clearNext();
            }
        });
        this.activity = activity;
    }

    public boolean isPlaying(){

        return mediaPlayer.isPlaying(); //TODO
    }

    public void seekTo(double percent){

        mediaPlayer.seekTo((int) (mediaPlayer.getDuration() * percent / 100));
    }

    public void pause(){
        if(isPlaying()){
            mediaPlayer.pause();
            paused = true;
        }
    }

    public void resume(){
        if(paused){
            mediaPlayer.start();
            paused = false;
        }
    }


    public boolean play(Song song){
        if(song == null){
            return false;
        }
        paused = false;
        mediaPlayer.reset();
        AssetFileDescriptor assetFileDescriptor = activity.getResources().openRawResourceFd(song.getID());
        try{
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepare();

        }
        catch (Exception e){
            return false;
        }


        timestamp = OffsetDateTime.now().minusHours(8);

        song.setPreviousDate(song.getCurrentDate());

        song.setCurrentDate(timestamp);

        mediaPlayer.start();
        return true;
    }

    public boolean playNext(Song song){
        if(nextSong == null){
            nextSong = song;
            return true;
        }
        return false;
    }

    public void clearNext(){
        nextSong = null;
    }
    public void clear(){
        clearNext();
        if(isPlaying()){
            stop();
        }
    }

    public void stop(){
        mediaPlayer.stop();
        mediaPlayer.reset();
    }
    public void setVolume(int volume){
        mediaPlayer.setVolume(volume, volume);
    }
    public void setEndListener(final SongPlayerCallback callback){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                callback.callback();
            }
        });
    }
    public interface SongPlayerCallback {
        public abstract void callback();
    }


}
