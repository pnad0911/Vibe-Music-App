package cse_110.flashback_player;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Patrick on 2/7/2018.
 */

public class SongPlayer {

    MediaPlayer mediaPlayer;
    AppCompatActivity activity;
    Song nextSong;

    public SongPlayer(AppCompatActivity activity){
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

    public void pause(){
        if(!isPlaying()){
            mediaPlayer.pause();
        }
    }

    public boolean play(Song song){
        if(song == null){
            return false;
        }
        mediaPlayer.reset();
        AssetFileDescriptor assetFileDescriptor = activity.getResources().openRawResourceFd(song.getID());
        try{
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepareAsync();
            mediaPlayer.start();
            return true;
        }
        catch (Exception e){
            return false;
        }
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
