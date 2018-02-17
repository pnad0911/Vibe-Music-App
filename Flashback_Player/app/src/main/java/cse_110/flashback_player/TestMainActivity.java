package cse_110.flashback_player;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class TestMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SongPlayer songPlayer = new SongPlayer(this);

        final Song susume_tomorrow = new Song("Susume Tomorrow", R.raw.after_the_storm, "Sonoda Umi", "Susume Tomorrow");
        final Song soldier_game = new Song("Soldier Game", R.raw.at_midnight, "Sonoda Umi, Nishikino Maki, Ayase Eli", "Soldier Game");
        final Song mp5_sound = new Song("mp5_sound", R.raw.america_religious, "", "");
        Button playbutton = null; //= (Button) findViewById(R.id.playbutton);
        Button pausebutton = null; //= (Button) findViewById(R.id.pausebutton);
        Button resetbutton = null; //= (Button) findViewById(R.id.resetbutton);
        playbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                songPlayer.play(soldier_game);
                songPlayer.playNext(susume_tomorrow);
            }
        });
        pausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(songPlayer.isPlaying()){
                    songPlayer.pause();
                }else{
                    songPlayer.resume();
                }
            }
        });
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songPlayer.stop();
            }
        });
    }

}
