package cse_110.flashback_player;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;
import static android.os.UserHandle.readFromParcel;


/**
 * Created by Patrick on 2/7/2018.
 */

public class SongPlayer implements Parcelable{

    private MediaPlayer mediaPlayer;
    private Activity activity;
    private Song nextSong;
    private List<SongPlayerCallback> callbackList;
    private int paused = 0;
    private MapsActivity mapsActivity;

    /**
     * Creates a new SongPlayer object attached to the given activity
     * @param activity Activity this SongPlayer is attached to.
     */
    public SongPlayer(Activity activity) {
        callbackList = new LinkedList<>();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                play(nextSong); //TODO change if mediaplayer is choppy after songs
                for (SongPlayerCallback cb:callbackList) {
                    cb.callback();
                }
                clearNext();
            }
        });
        mapsActivity = new MapsActivity();
        this.activity = activity;
    }


    public boolean isPlaying(){

        return mediaPlayer.isPlaying(); //TODO
    }

    public void seekTo(double percent){

        mediaPlayer.seekTo((int) (mediaPlayer.getDuration() * percent / 100));
    }

    public boolean isPaused(){
        return (paused == 1);
    }

    public void pause(){
        if(isPlaying()){
            mediaPlayer.pause();
            paused = 1;
        }
    }

    public void resume(){
        if(isPaused()){
            mediaPlayer.start();
            paused = 0;
        }
    }


    public boolean play(Song song){
        if(song == null){
            return false;
        }
        paused = 0;
        mediaPlayer.reset();
        AssetFileDescriptor assetFileDescriptor = activity.getResources().openRawResourceFd(song.getID());
        try{
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepare();

        }
        catch (Exception e){
            return false;
        }
        song.setPreviousLocation(song.getLoc_lat(),song.getLoc_long());
        song.setPreviousDate(song.getDate());
        song.setLocation(mapsActivity.getLoc_lat(), mapsActivity.getLoc_long());
        song.setDate(mapsActivity.getDate());

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
        callbackList.add(callback);
    }

    public interface SongPlayerCallback {
        public abstract void callback();
    }

    /*--------------------------------------------------------------
     * Begin required override methods from implementing Parcelable
     */

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeValue(mediaPlayer);
        out.writeValue(activity);
        out.writeValue(nextSong);
        out.writeInt(paused);
    }

    public static final Parcelable.Creator<SongPlayer> CREATOR
            = new Parcelable.Creator<SongPlayer>() {
        public SongPlayer createFromParcel(Parcel in) {
            return new SongPlayer(in);
        }

        public SongPlayer[] newArray(int size) {
            return new SongPlayer[size];
        }
    };

    private SongPlayer(Parcel in) {
        mediaPlayer = (MediaPlayer) in.readValue(null);
        activity = (Activity) in.readValue(null);
        nextSong = (Song) in.readValue(null);
        paused = in.readInt();
    }

    /* ENDS ---------------------------------------------------*/


}
