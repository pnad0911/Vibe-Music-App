package cse_110.flashback_player;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Patrick on 2/7/2018.
 */

public class SongPlayer {

    private MediaPlayer mediaPlayer;
    private AppCompatActivity activity;
    private Song nextSong;
    private boolean paused = false;

    /**
     * Creates a new SongPlayer object attached to the given activity
     * @param activity Activity this SongPlayer is attached to.
     */
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
