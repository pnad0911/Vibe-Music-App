//package cse_110.flashback_player;
//
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import org.w3c.dom.Text;
//
//public class TestMainActivity extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        final SongPlayer songPlayer = new SongPlayer(this);
//        final Song susume_tomorrow = new Song("Susume Tomorrow", R.raw.susume_tomorrow, "Sonoda Umi", "Susume Tomorrow");
//        final Song soldier_game = new Song("Soldier Game", R.raw.origin, "Sonoda Umi, Nishikino Maki, Ayase Eli", "Soldier Game");
//        final Song mp5_sound = new Song("mp5_sound", R.raw.mp5_sound, "", "");
//        final TextView finishText = (TextView) findViewById(R.id.finishText);
//
//        /*
//         * Use the setEndListener() method with an inline class implementing the interface SongPlayer.SongPlayerCallback to declare a listener
//         * Inside callback(), put whatever code you want executed when a song finishes playing. In this case, it simply displays "Song finished"
//         * under the button row
//         */
//        songPlayer.setEndListener(new SongPlayer.SongPlayerCallback() {
//            @Override
//            public void callback() {
//                finishText.setText("Song finished");
//            }
//        });
//
//
//        Button playbutton = (Button) findViewById(R.id.playbutton);
//        Button pausebutton = (Button) findViewById(R.id.pausebutton);
//        Button resetbutton = (Button) findViewById(R.id.resetbutton);
//
//        playbutton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                songPlayer.play(mp5_sound);
//                songPlayer.playNext(susume_tomorrow);
//            }
//        });
//        pausebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(songPlayer.isPlaying()){
//                    songPlayer.pause();
//                }else{
//                    songPlayer.resume();
//                }
//            }
//        });
//        resetbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                songPlayer.stop();
//            }
//        });
//    }
//
//}
//
